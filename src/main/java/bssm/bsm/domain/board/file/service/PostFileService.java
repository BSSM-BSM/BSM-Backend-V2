package bssm.bsm.domain.board.file.service;

import bssm.bsm.domain.board.file.exception.FileUploadException;
import bssm.bsm.domain.board.post.presentation.dto.res.UploadFileRes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class PostFileService {

    @Value("${env.file.path.base}")
    private String PUBLIC_RESOURCE_PATH;
    @Value("${env.file.path.upload.board}")
    private String BOARD_UPLOAD_RESOURCE_PATH;

    public UploadFileRes uploadFile(MultipartFile file) {
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
        String fileId = String.valueOf(new Date().getTime());
        File dir = new File(PUBLIC_RESOURCE_PATH + BOARD_UPLOAD_RESOURCE_PATH);
        File newFile = new File(dir.getPath() + "/" + fileId + "." + fileExt);

        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                throw new FileUploadException();
            }
        }

        try {
            file.transferTo(newFile);
            return UploadFileRes.create(fileId, fileExt);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileUploadException();
        }
    }

}
