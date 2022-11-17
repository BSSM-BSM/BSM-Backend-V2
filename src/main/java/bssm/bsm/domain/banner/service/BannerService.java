package bssm.bsm.domain.banner.service;

import bssm.bsm.domain.banner.domain.Banner;
import bssm.bsm.domain.banner.domain.BannerRepository;
import bssm.bsm.domain.banner.presentation.dto.response.BannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    public List<BannerResponse> getBannerList() {
        return bannerRepository.findAll().stream()
                .map(Banner::toResponse)
                .toList();
    }

}
