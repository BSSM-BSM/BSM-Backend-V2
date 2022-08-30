package bssm.bsm.domain.user.repositories;

import bssm.bsm.domain.user.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository <Student, String> {

    List<Student> findByGradeNot(int grade);

    Optional<Student> findByGradeAndClassNoAndStudentNo(int grade, int classNo, int studentNo);

    Optional<Student> findByEnrolledAtAndGradeAndClassNoAndStudentNo(int enrolledAt, int grade, int classNo, int studentNo);
}
