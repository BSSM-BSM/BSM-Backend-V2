package bssm.bsm.domain.user.facade;

import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.repository.StudentRepository;
import bssm.bsm.domain.user.exception.NoSuchStudentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentFacade {

    private final StudentRepository studentRepository;

    public Student find(Integer enrolledAt, Integer grade, Integer classNo, Integer studentNo) {
        return studentRepository.findByEnrolledAtAndGradeAndClassNoAndStudentNo(enrolledAt, grade, classNo, studentNo)
                .orElseThrow(NoSuchStudentException::new);
    }

}
