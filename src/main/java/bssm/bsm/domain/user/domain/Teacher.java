package bssm.bsm.domain.user.domain;

import bssm.bsm.domain.user.presentation.dto.response.StudentInfoResponse;
import bssm.bsm.domain.user.presentation.dto.response.TeacherInfoResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    @Column(length = 8)
    private String name;

    @Column(length = 32)
    private String email;

    @Builder
    public Teacher(Long teacherId, String name, String email) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
    }

    public TeacherInfoResponse toInfo() {
        return TeacherInfoResponse.builder()
                .name(name)
                .build();
    }

}
