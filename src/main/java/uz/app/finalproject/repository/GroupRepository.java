package uz.app.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.finalproject.entity.Groups;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Groups , Long> {

    Optional<Groups> findByStatus(String status);
    Boolean existsByGroupNameAndStatusNot(String name , String status);
    List<Groups> findAllByGroupNameContainsAndStatusEquals(String name , String status);
}
