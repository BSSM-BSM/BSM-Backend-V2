package bssm.bsm.domain.user.repositories;

import bssm.bsm.domain.user.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TeacherRepository extends JpaRepository <Teacher, String> {

    Optional<Teacher> findByEmail(String email);
}
