package bssm.bsm.domain.board.emoticon;

import bssm.bsm.domain.board.emoticon.dto.request.EmoticonDeleteRequest;
import bssm.bsm.domain.board.emoticon.dto.request.EmoticonUploadRequest;
import bssm.bsm.domain.board.emoticon.dto.response.EmoticonResponse;
import bssm.bsm.domain.board.emoticon.entities.Emoticon;
import bssm.bsm.domain.board.emoticon.entities.EmoticonItem;
import bssm.bsm.domain.board.emoticon.entities.EmoticonItemPk;
import bssm.bsm.domain.board.emoticon.repositories.EmoticonItemRepository;
import bssm.bsm.domain.board.emoticon.repositories.EmoticonRepository;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.global.error.exceptions.BadRequestException;
import bssm.bsm.global.error.exceptions.ConflictException;
import bssm.bsm.global.error.exceptions.InternalServerException;
import bssm.bsm.global.error.exceptions.NotFoundException;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class EmoticonService {

    private final EmoticonRepository emoticonRepository;
    private final EmoticonItemRepository emoticonItemRepository;
    private final List<String> allowedExt = List.of(new String[]{
            "png",
            "jpg",
            "jpeg",
            "gif",
            "webp"
    });
    @Value("${env.file.path.base}")
    private String PUBLIC_RESOURCE_PATH;
    @Value("${env.file.path.upload.emoticon}")
    private String EMOTICON_UPLOAD_PATH;

    public EmoticonResponse getEmoticon(long id) {
        return emoticonRepository.findById(id).orElseThrow(
                () -> {throw new NotFoundException("이모티콘을 찾을 수 없습니다");}
        ).toDto();
    }

    public List<EmoticonResponse> getEmoticonList() {
        List<Emoticon> emoticonList = emoticonRepository.findAllByActiveAndDeleted(true, false);
        return emoticonList
                .stream().map(Emoticon::toDto)
                .collect(Collectors.toList());
    }

    public List<EmoticonResponse> getInactiveEmoticonList() {
        List<Emoticon> emoticonList = emoticonRepository.findAllByActiveAndDeleted(false, false);
        return emoticonList
                .stream().map(Emoticon::toDto)
                .collect(Collectors.toList());
    }

    public void activeEmoticon(long id) {
        Emoticon emoticon = emoticonRepository.findById(id).orElseThrow(
                () -> {throw new NotFoundException("이모티콘을 찾을 수 없습니다.");}
        );
        emoticon.setActive(true);
        emoticonRepository.save(emoticon);
    }

    @Transactional
    public void deleteEmoticon(long id, EmoticonDeleteRequest dto) {
        Emoticon emoticon = emoticonRepository.findById(id).orElseThrow(
                () -> {throw new NotFoundException("이모티콘을 찾을 수 없습니다.");}
        );
        emoticon.setDeleted(true);
        emoticon.setDeleteReason(dto.getMsg());
        emoticonRepository.save(emoticon);
    }

    @Transactional
    public void upload(User user, @Valid EmoticonUploadRequest dto) throws IOException {
        emoticonValidateCheck(dto);
        Emoticon emoticonInfo = saveEmoticonInfo(user, dto);
        List<EmoticonItem> emoticonItems = saveEmoticonItems(emoticonInfo, dto);

        File dir = new File(PUBLIC_RESOURCE_PATH + EMOTICON_UPLOAD_PATH + "/" + emoticonInfo.getId());
        if (!dir.exists()) {
            try {
                dir.mkdirs();
                dto.getThumbnail().transferTo(new File(dir.getPath() + "/0.png"));
            } catch (IOException e) {
                e.printStackTrace();
                // 에러 발생시 폴더 및 파일 삭제
                Files.walk(dir.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                throw new InternalServerException("이모티콘 업로드에 실패하였습니다");
            }
        }

        emoticonItems.forEach(emoticon -> {
            int idx = emoticon.getPk().getIdx(); // idx > 0
            MultipartFile file = dto.getEmoticonList().get(idx-1);
            try {
                file.transferTo(new File(dir.getPath() + "/" + idx + "." + emoticon.getType()));
            } catch (IOException e) {
                e.printStackTrace();
                // 에러 발생시 폴더 및 파일 삭제
                try {
                    Files.walk(dir.toPath())
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                throw new InternalServerException("이모티콘 업로드에 실패하였습니다");
            }
        });
    }

    private void emoticonValidateCheck(EmoticonUploadRequest dto) {
        dto.getEmoticonList().forEach(file -> {
            String fileExt = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
            if (!allowedExt.contains(fileExt))
                throw new BadRequestException(ImmutableMap.<String, String>builder().
                        put("file", "파일의 확장자가 올바르지 않습니다").
                        build()
                );
        });
    }

    private Emoticon saveEmoticonInfo(User user, EmoticonUploadRequest dto) {
        if (emoticonRepository.existsByName(dto.getName())) throw new ConflictException("해당 이름의 이모티콘이 이미 존재합니다");
        return emoticonRepository.save(
                Emoticon.builder()
                        .active(false)
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .userCode(user.getCode())
                        .build()
        );
    }

    private List<EmoticonItem> saveEmoticonItems(Emoticon emoticon, EmoticonUploadRequest dto) {
        List<EmoticonItem> emoticonItems = new ArrayList<>();
        for (int i=0; i<dto.getEmoticonList().size(); i++) {
            MultipartFile file = dto.getEmoticonList().get(i);
            String fileExt = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
            emoticonItems.add(
                    EmoticonItem.builder()
                            .pk(EmoticonItemPk.builder()
                                    .emoticon(emoticon)
                                    .idx(i+1)
                                    .build())
                            .type(fileExt)
                            .build()
            );
        }

        return emoticonItemRepository.saveAll(emoticonItems);
    }

}
