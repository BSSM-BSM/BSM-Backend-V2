package bssm.bsm.user.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private long usercode;

    @Column(nullable = false, length = 20, unique = true)
    private String id;

    @Column(nullable = false, length = 40, unique = true)
    private String nickname;

    @Column(name = "uniq_no", nullable = false, length = 10)
    private String uniqNo;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "created_at", nullable = false, length = 40)
    private Date createdAt;

    @Column(nullable = false, length = 64)
    private String pw;

    @Column(name = "pw_salt", nullable = false, length = 64)
    private String pwSalt;
}
