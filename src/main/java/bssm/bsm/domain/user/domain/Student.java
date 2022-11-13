package bssm.bsm.domain.user.domain;

import bssm.bsm.domain.user.presentation.dto.response.StudentInfoResponse;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @Column(length = 10)
    private String studentId;

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

    @OneToOne(mappedBy = "student")
    @org.springframework.data.annotation.Transient
    private User user;

    @Builder
    public Student(String studentId, int enrolledAt, int grade, int classNo, int studentNo, String name, String email, User user) {
        this.studentId = studentId;
        this.enrolledAt = enrolledAt;
        this.grade = grade;
        this.classNo = classNo;
        this.studentNo = studentNo;
        this.name = name;
        this.email = email;
        this.user = user;
    }

    public void setEnrolledAt(int enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setClassNo(int classNo) {
        this.classNo = classNo;
    }

    public void setStudentNo(int studentNo) {
        this.studentNo = studentNo;
    }

    public StudentInfoResponse toInfo() {
        return StudentInfoResponse.builder()
                .name(name)
                .enrolledAt(enrolledAt)
                .grade(grade)
                .classNo(classNo)
                .studentNo(studentNo)
                .build();
    }

}
