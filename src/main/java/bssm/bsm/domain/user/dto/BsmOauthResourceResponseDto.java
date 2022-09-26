package bssm.bsm.domain.user.dto;

import bssm.bsm.domain.user.type.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class BsmOauthResourceResponseDto {

    private Long userCode;
    private UserRole role;
    private studentDto student;
    private teacherDto teacher;

    @JsonProperty("user")
    private void unpackNested(Map<String, Object> user) {
        this.userCode = Long.valueOf(user.get("code").toString());
        this.role = UserRole.valueOf(user.get("role").toString());
        switch (this.role) {
            case STUDENT -> {
                this.student = studentDto.builder()
                        .userCode(this.userCode)
                        .nickname((String) user.get("nickname"))
                        .enrolledAt((int) user.get("enrolledAt"))
                        .grade((int) user.get("grade"))
                        .classNo((int) user.get("classNo"))
                        .studentNo((int) user.get("studentNo"))
                        .build();
            }
            case TEACHER -> {
                this.teacher = teacherDto.builder()
                        .userCode(this.userCode)
                        .nickname((String) user.get("nickname"))
                        .email((String) user.get("email"))
                        .name((String) user.get("name"))
                        .build();
                System.out.println("(String) user.get(\"email\") = " + (String) user.get("email"));
                System.out.println("this.teacher.getEmail() = " + this.teacher.getEmail());
            }
        }
    }
}
