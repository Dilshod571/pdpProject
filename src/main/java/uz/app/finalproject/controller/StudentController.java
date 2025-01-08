package uz.app.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.finalproject.dto.UserDTO;
import uz.app.finalproject.entity.Groups;
import uz.app.finalproject.entity.ResponseMessage;
import uz.app.finalproject.entity.User;
import uz.app.finalproject.repository.GroupRepository;
import uz.app.finalproject.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    final UserRepository userRepository;
    final GroupRepository groupRepository;



    @PostMapping("/add{groupId}")
    public ResponseEntity<?> addStudent(@RequestBody UserDTO userDto, @PathVariable String groupId) {

        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setPhoneNumber(String.valueOf(userDto.getPhoneNumber()));
        user.setRole("STUDENT");
        user.setPassword(userDto.getPassword());
        Optional<Groups> byId = groupRepository.findById(Long.valueOf(groupId));

        if ( byId.isPresent() ){
            Groups groups = byId.get();
            userRepository.save(user);

            groups.getStudents().add(user);
            groups.setStNumber(groups.getStudents().size());
            groupRepository.save(groups);

            return ResponseEntity.status(200).body(new ResponseMessage("Student added to group", userDto, true));
        }

        return ResponseEntity.status(404).body(new ResponseMessage("Group not found", null, false));

    }


    @GetMapping
    public ResponseEntity<?> getStudent() {
        List<User> student = userRepository.findAllByRoleAndStatusNot("STUDENT" , "ARXIV");


        return ResponseEntity.ok(new ResponseMessage("All students", student, true));

    }

    @GetMapping("/arxiv")
    public ResponseEntity<?> getArxiv() {
        List<User> arxiv = userRepository.findAllByRoleAndStatusNot("ARXIV" , "ARXIV");

        return ResponseEntity.ok(new ResponseMessage("All arxiv", arxiv, true));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody UserDTO userDto, @PathVariable Integer id) {
        Optional<User> student = userRepository.findById(Long.valueOf(id));
        if (student.isPresent()) {
            User user = student.get();
            user.setFirstname(userDto.getFirstname());
            user.setLastname(userDto.getLastname());
            user.setPassword(userDto.getPassword());
            user.setRole(userDto.getRole());
            user.setPhoneNumber(String.valueOf(userDto.getPhoneNumber()));
            userRepository.save(user);
            return ResponseEntity.status(200).body(new ResponseMessage("Student updated", userDto, true));
        }

        return ResponseEntity.status(404).body(new ResponseMessage("Student not found", null, true));

    }




    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        Optional<User> byId = userRepository.findById(Long.valueOf(id));

        if ( byId.isPresent() ) {
            User user = byId.get();

            if (user.getStatus().equals("ACTIVE")) {
                ;
                user.setStatus("ARXIV");
                userRepository.save(user);
                return ResponseEntity.status(200).body(new ResponseMessage("Student deleted", null, true));

            }
            return ResponseEntity.status(409).body(new ResponseMessage("Student not found", null, false));
        }

        return ResponseEntity.status(409).body(new ResponseMessage("Student not found", null, false));
    }


    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String search){
        userRepository.findAllByFirstnameContainingOrLastnameContainingAndStatusAndRole( search , search , "ACTIVE" , "STUDENT");
        return ResponseEntity.status(200).body(new ResponseMessage("Result", userRepository.findAllByFirstnameContainingOrLastnameContainingAndStatusAndRole( search, search, "ACTIVE", "STUDENT"), true));
    }


}