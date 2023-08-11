package bssm.bsm.domain.board.like.presentation;

import bssm.bsm.domain.board.like.presentation.dto.req.LikeReq;
import bssm.bsm.domain.board.like.presentation.dto.res.LikeRes;
import bssm.bsm.domain.board.like.service.LikeService;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("like")
@RequiredArgsConstructor
public class LikeController {

    private final CurrentUser currentUser;
    private final LikeService likeService;

    @PostMapping
    public LikeRes like(@RequestBody LikeReq req) {
        return likeService.like(currentUser.getUser(), req);
    }
}
