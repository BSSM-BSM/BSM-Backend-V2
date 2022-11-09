package bssm.bsm.domain.user.facade;

import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.repository.StudentRepository;
import bssm.bsm.domain.user.presentation.dto.response.UserResponse;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final StudentRepository studentRepository;

    public UserResponse toBoardUserResponse(User user, boolean anonymous) {
        if (anonymous) {
            return UserResponse.builder()
                    .code((long) -1)
                    .nickname("ㅇㅇ")
                    .build();
        }
        return UserResponse.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }

    public List<User> findAllByGradeAndClassNo(int grade, int classNo) {
        return studentRepository.findAllByGradeAndClassNo(grade, classNo)
                .stream().map(Student::getUser)
                .toList();
    }

}
