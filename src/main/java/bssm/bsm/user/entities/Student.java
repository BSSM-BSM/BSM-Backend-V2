package bssm.bsm.user.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "uniq_no", length = 10)
    private String uniqNo;

    @Column(name = "code_available", columnDefinition = "INT(1) UNSIGNED")
    private boolean codeAvailable;

    @Column(name = "auth_code", length = 8)
    private String authCode;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private int level;

    @Column(name = "enrolled_at", columnDefinition = "year")
    private int enrolledAt;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private int grade;

    @Column(name = "class_no", columnDefinition = "INT(1) UNSIGNED")
    private int classNo;

    @Column(columnDefinition = "INT(2) UNSIGNED")
    private int studentNo;

    @Column(length = 8)
    private String name;

    @Column(length = 32)
    private String email;

    public void setCodeAvailable(boolean codeAvailable) {
        this.codeAvailable = codeAvailable;
    }
}
