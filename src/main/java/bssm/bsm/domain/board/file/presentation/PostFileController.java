package bssm.bsm.domain.board.file.presentation;

import bssm.bsm.domain.board.file.service.PostFileService;
import bssm.bsm.domain.board.post.presentation.dto.res.UploadFileRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("post/file")
@RequiredArgsConstructor
public class PostFileController {

    private final PostFileService postFileService;

    @PostMapping
    public UploadFileRes uploadFile(@RequestParam MultipartFile file) {
        return postFileService.uploadFile(file);
    }
}
