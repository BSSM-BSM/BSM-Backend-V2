package bssm.bsm.domain.user.domain;

import bssm.bsm.domain.user.presentation.dto.response.TeacherInfoResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("teacher")
public class Teacher {

    @Id // jpa
    @org.springframework.data.annotation.Id // redis
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
