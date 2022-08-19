package bssm.bsm.user.repositories;

import bssm.bsm.user.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository <Student, String> {

    Optional<Student> findByGradeAndClassNoAndStudentNo(int grade, int classNo, int studentNo);

    Optional<Student> findByEnrolledAtAndGradeAndClassNoAndStudentNo(int enrolledAt, int grade, int classNo, int studentNo);
}
