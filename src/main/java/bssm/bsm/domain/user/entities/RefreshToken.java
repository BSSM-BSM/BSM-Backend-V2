package bssm.bsm.domain.user.entities;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
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
}
