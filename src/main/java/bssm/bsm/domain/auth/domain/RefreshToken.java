package bssm.bsm.domain.auth.domain;

import bssm.bsm.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(length = 64)
    private String token;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private Date createdAt;

    @Builder
    public RefreshToken(String token, boolean isAvailable, User user, Date createdAt) {
        this.token = token;
        this.isAvailable = isAvailable;
        this.user = user;
        this.createdAt = createdAt;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

}
