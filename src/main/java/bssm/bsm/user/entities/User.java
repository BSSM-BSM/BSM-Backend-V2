package bssm.bsm.user.entities;

import bssm.bsm.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Entity
@Table(name = "user_tbl")
public class User extends BaseTimeEntity {

    @Id
    @Column(columnDefinition = "INT UNSIGNED")
    private int usercode;

    @Column(nullable = false, length = 40, unique = true)
    private String nickname;

    @Column(nullable = false, length = 10)
    private String uniqNo;

    @OneToOne
    @JoinColumn(name = "uniqNo", nullable = false, insertable = false, updatable = false)
    private Student student;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false, length = 32)
    private String oauthToken;
}
