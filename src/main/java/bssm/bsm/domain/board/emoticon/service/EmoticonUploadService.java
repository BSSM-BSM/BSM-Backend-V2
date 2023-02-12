package bssm.bsm.domain.board.emoticon.service;

import bssm.bsm.domain.board.emoticon.domain.Emoticon;
import bssm.bsm.domain.board.emoticon.domain.EmoticonItem;
import bssm.bsm.domain.board.emoticon.domain.repository.EmoticonItemRepository;
import bssm.bsm.domain.board.emoticon.domain.repository.EmoticonRepository;
import bssm.bsm.domain.board.emoticon.exception.EmoticonFileUploadException;
import bssm.bsm.domain.board.emoticon.exception.NotAllowedEmoticonFileExtensionException;
import bssm.bsm.domain.board.emoticon.presentation.dto.req.EmoticonUploadReq;
import bssm.bsm.domain.user.domain.User;
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
public class EmoticonUploadService {

    private final EmoticonProvider emoticonProvider;
    private final EmoticonRepository emoticonRepository;
    private final EmoticonItemRepository emoticonItemRepository;

    private final List<String> allowExtList = List.of(new String[]{
            "png",
            "jpg",
            "jpeg",
            "gif",
            "webp"
    });
    private final String EMOTICON_THUMBNAIL_FILE_NAME = "0.png";

    @Value("${env.file.path.base}")
    private String PUBLIC_RESOURCE_PATH;
    @Value("${env.file.path.upload.emoticon}")
    private String EMOTICON_UPLOAD_PATH;

    public void upload(User user, @Valid EmoticonUploadReq req) {
        emoticonItemValidate(req.getEmoticonList());
        Emoticon emoticon = saveEmoticon(user, req);
        List<EmoticonItem> emoticonItems = saveEmoticonItems(emoticon, req);
        emoticonFileUpload(emoticon, req.getThumbnail(), emoticonItems, req.getEmoticonList());
    }

    private void emoticonItemValidate(List<MultipartFile> emoticonItemList) {
        List<Integer> invalidFileIdxList = new ArrayList<>();
        for (int idx=0; idx<emoticonItemList.size(); idx++) {
            MultipartFile file = emoticonItemList.get(idx);
            if (!emoticonItemCheck(file)) invalidFileIdxList.add(idx);
        }
        if (invalidFileIdxList.size() > 0) throw new NotAllowedEmoticonFileExtensionException(invalidFileIdxList);
    }

    private boolean emoticonItemCheck(MultipartFile emoticonItem) {
        String fileName = Objects.requireNonNull(emoticonItem.getOriginalFilename());
        String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
        return allowExtList.contains(fileExt);
    }

    private Emoticon saveEmoticon(User user, EmoticonUploadReq req) {
        emoticonProvider.duplicateEmoticonNameCheck(req.getName());
        Emoticon emoticon = Emoticon.create(req.getName(), req.getDescription(), user);
        return emoticonRepository.save(emoticon);
    }

    private List<EmoticonItem> saveEmoticonItems(Emoticon emoticon, EmoticonUploadReq req) {
        List<EmoticonItem> emoticonItems = new ArrayList<>();
        for (int i=0; i<req.getEmoticonList().size(); i++) {
            MultipartFile file = req.getEmoticonList().get(i);
            String fileName = Objects.requireNonNull(file.getOriginalFilename());
            String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
            int itemIdx = i + 1;
            emoticonItems.add(EmoticonItem.create(emoticon, itemIdx, fileExt));
        }

        return emoticonItemRepository.saveAll(emoticonItems);
    }

    private void emoticonFileUpload(
            Emoticon emoticon,
            MultipartFile thumbnailFile,
            List<EmoticonItem> emoticonItems,
            List<MultipartFile> emoticonItemFiles
    ) {
        File dir = new File(PUBLIC_RESOURCE_PATH + EMOTICON_UPLOAD_PATH + "/" + emoticon.getId());
        thumbnailFileUpload(dir, thumbnailFile);

        emoticonItems.forEach(item -> {
            int idx = item.getPk().getIdx(); // idx > 0
            MultipartFile file = emoticonItemFiles.get(idx - 1);
            itemFileUpload(dir, item, file);
        });
    }

    private void thumbnailFileUpload(File dir, MultipartFile thumbnailFile) {
        try {
            dir.mkdirs();
            thumbnailFile.transferTo(new File(dir.getPath() + "/" + EMOTICON_THUMBNAIL_FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
            revertFileUploadAndThrow(dir);
        }
    }

    private void itemFileUpload(File dir, EmoticonItem item, MultipartFile file) {
        int idx = item.getPk().getIdx();
        String fileName = idx + "." + item.getType();
        try {
            file.transferTo(new File(dir.getPath() + "/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            revertFileUploadAndThrow(dir);
        }
    }

    private void revertFileUploadAndThrow(File dir) {
        // 업로드 에러 발생시 폴더 및 파일 삭제
        try {
            Files.walk(dir.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new EmoticonFileUploadException();
    }

}
