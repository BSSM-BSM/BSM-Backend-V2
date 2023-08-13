package bssm.bsm.domain.banner.domain;

import bssm.bsm.domain.banner.presentation.dto.response.BannerResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner {

    @Id
    @Column(length = 16)
    private String id;

    @Column(length = 5)
    private String ext;

    @Column
    private String link;

    public BannerResponse toResponse() {
        return BannerResponse.builder()
                .id(id)
                .ext(ext)
                .link(link)
                .build();
    }

}
