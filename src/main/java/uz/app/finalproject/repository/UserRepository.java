package uz.app.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.finalproject.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByFirstnameAndLastname(String firstname , String lastName);
    List<User> findAllByRoleAndStatusNot(String role , String status);
    List<User> findAllByRoleNotInAndStatus(List<String> roles , String status);
    List<User> findAllByRoleNotAndStatus(String role, String status);
    List<User> findAllByFirstnameContainingOrLastnameContainingAndStatusAndRole(String firstname, String lastname, String status , String role);


}
