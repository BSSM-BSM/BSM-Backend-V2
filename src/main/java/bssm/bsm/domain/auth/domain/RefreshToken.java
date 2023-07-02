package bssm.bsm.domain.auth.domain;

import bssm.bsm.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(length = 64)
    private String token;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne
    @JoinColumn(name = "userCode", nullable = false, insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private Date createdAt;

    @Builder
    public RefreshToken(String token, boolean isAvailable, Long userCode, User user, Date createdAt) {
        this.token = token;
        this.isAvailable = isAvailable;
        this.userCode = userCode;
        this.user = user;
        this.createdAt = createdAt;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

}
