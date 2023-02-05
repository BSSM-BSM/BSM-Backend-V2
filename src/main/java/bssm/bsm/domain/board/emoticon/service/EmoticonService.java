package bssm.bsm.domain.board.emoticon.service;

import bssm.bsm.domain.board.emoticon.exception.EmoticonFileUploadException;
import bssm.bsm.domain.board.emoticon.exception.NoSuchEmoticonException;
import bssm.bsm.domain.board.emoticon.presentation.dto.req.EmoticonUploadReq;
import bssm.bsm.domain.board.emoticon.presentation.dto.res.EmoticonRes;
import bssm.bsm.domain.board.emoticon.domain.Emoticon;
import bssm.bsm.domain.board.emoticon.domain.EmoticonItem;
import bssm.bsm.domain.board.emoticon.domain.repository.EmoticonItemRepository;
import bssm.bsm.domain.board.emoticon.domain.repository.EmoticonRepository;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.error.exceptions.BadRequestException;
import bssm.bsm.global.error.exceptions.ConflictException;
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

@Service
@Validated
@Transactional
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

    @Transactional(readOnly = true)
    public EmoticonRes getEmoticon(long id) {
        return emoticonRepository.findById(id)
                .orElseThrow(NoSuchEmoticonException::new)
                .toResponse();
    }

    @Transactional(readOnly = true)
    public List<EmoticonRes> getEmoticonList() {
        List<Emoticon> emoticonList = emoticonRepository.findAllByActiveAndDeleted(true, false);
        return emoticonList.stream()
                .map(Emoticon::toResponse)
                .toList();
    }

    public void upload(User user, @Valid EmoticonUploadReq dto) throws IOException {
        emoticonValidate(dto);
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
                throw new EmoticonFileUploadException();
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
                throw new EmoticonFileUploadException();
            }
        });
    }

    private void emoticonValidate(EmoticonUploadReq dto) {
        dto.getEmoticonList().forEach(file -> {
            String fileExt = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
            if (!allowedExt.contains(fileExt))
                throw new BadRequestException(ImmutableMap.<String, String>builder().
                        put("file", "파일의 확장자가 올바르지 않습니다").
                        build()
                );
        });
    }

    private Emoticon saveEmoticonInfo(User user, EmoticonUploadReq dto) {
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

    private List<EmoticonItem> saveEmoticonItems(Emoticon emoticon, EmoticonUploadReq dto) {
        List<EmoticonItem> emoticonItems = new ArrayList<>();
        for (int i=0; i<dto.getEmoticonList().size(); i++) {
            MultipartFile file = dto.getEmoticonList().get(i);
            String fileName = Objects.requireNonNull(file.getOriginalFilename());
            String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
            int itemIdx = i + 1;
            emoticonItems.add(EmoticonItem.create(emoticon, itemIdx, fileExt));
        }

        return emoticonItemRepository.saveAll(emoticonItems);
    }

}
