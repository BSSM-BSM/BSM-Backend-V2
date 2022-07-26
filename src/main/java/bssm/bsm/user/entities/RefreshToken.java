package bssm.bsm.user.entities;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class RefreshToken {

    @Id
    @Column(length = 64)
    private String token;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(columnDefinition = "INT UNSIGNED")
    private int usercode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private Date createdAt;
}
