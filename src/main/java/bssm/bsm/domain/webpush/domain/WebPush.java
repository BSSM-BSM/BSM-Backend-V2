package bssm.bsm.domain.webpush.domain;

import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.webpush.presentation.dto.request.WebPushSubscribeRequest;
import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebPush extends BaseTimeEntity {

    @Setter
    @Id
    @Column(length = 512)
    private String endpoint;

    @Setter
    @Column
    private String auth;

    @Setter
    @Column
    private String p256dh;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public WebPush(String endpoint, String auth, String p256dh, User user) {
        this.endpoint = endpoint;
        this.auth = auth;
        this.p256dh = p256dh;
        this.user = user;
    }

    public void updateWebPush(WebPushSubscribeRequest dto) {
        this.endpoint = dto.getEndpoint();
        this.auth = dto.getAuth();
        this.p256dh = dto.getP256dh();
    }

}
