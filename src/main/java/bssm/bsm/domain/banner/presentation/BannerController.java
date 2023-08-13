package bssm.bsm.domain.banner.presentation;

import bssm.bsm.domain.banner.presentation.dto.response.BannerResponse;
import bssm.bsm.domain.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public List<BannerResponse> getBannerList() {
        return bannerService.getBannerList();
    }

}
