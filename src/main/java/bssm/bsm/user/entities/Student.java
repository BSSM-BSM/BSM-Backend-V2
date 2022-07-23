package bssm.bsm.user.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Student {

    @Id
    @Column(length = 10)
    private String uniqNo;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private boolean codeAvailable;

    @Column(length = 8)
    private String authCode;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private int level;

    @Column(columnDefinition = "year")
    private int enrolledAt;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private int grade;

    @Column(columnDefinition = "INT(1) UNSIGNED")
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
