package bssm.bsm.domain.user.domain.repository;

import bssm.bsm.domain.user.domain.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository <Student, String> {

    List<Student> findByGradeNot(int grade);

    Optional<Student> findByGradeAndClassNoAndStudentNo(int grade, int classNo, int studentNo);

    Optional<Student> findByEnrolledAtAndGradeAndClassNoAndStudentNo(int enrolledAt, int grade, int classNo, int studentNo);

    @EntityGraph(attributePaths = "user")
    List<Student> findAllByGradeAndClassNo(int grade, int classNo);

}
