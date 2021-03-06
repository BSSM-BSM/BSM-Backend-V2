package bssm.bsm.user.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int usercode;

    @Column(nullable = false, length = 20, unique = true)
    private String id;

    @Column(nullable = false, length = 40, unique = true)
    private String nickname;

    @Column(nullable = false, length = 10)
    private String uniqNo;

    @OneToOne
    @JoinColumn(name = "uniqNo", insertable = false, updatable = false)
    private Student student;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false, length = 64)
    private String pw;

    @Column(nullable = false, length = 64)
    private String pwSalt;
}
