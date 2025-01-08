package uz.app.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.UserDatabase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.finalproject.dto.UserDTO;
import uz.app.finalproject.entity.ResponseMessage;
import uz.app.finalproject.entity.User;
import uz.app.finalproject.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    final UserRepository staffRepository;

    @GetMapping("/teacher")
    public ResponseEntity<?> getStaffs(){
        List<User> teachers = staffRepository.findAllByRoleAndStatusNot("TEACHER" , "ARXIV");
        return ResponseEntity.status(200).body(new ResponseMessage("All teachers" , teachers , true));
    }

    @GetMapping("/arxiv")
    public ResponseEntity<?> staffArxiv(){
        List<User> staff = staffRepository.findAllByRoleNotAndStatus("STUDENT" , "ARXIV");
        return ResponseEntity.status(200).body(new ResponseMessage("All arxiv staffs" , staff, true));
    }

    @GetMapping("/other")
    public ResponseEntity<?> staffOther(){
        List<User> staff = staffRepository.findAllByRoleNotInAndStatus(Arrays.asList("TEACHER" , "STUDENT") , "ACTIVE");
        return ResponseEntity.status(200).body(new ResponseMessage("All other staffs" , staff, true));
    }

    @PostMapping
    public ResponseEntity<?> addStaff(@RequestBody UserDTO staffDTO){
        User user = new User();
        user.setFirstname(staffDTO.getFirstname());
        user.setLastname(staffDTO.getLastname());
        user.setPhoneNumber(staffDTO.getPhoneNumber());
        user.setRole(staffDTO.getRole());
        user.setPassword(staffDTO.getPassword());
        staffRepository.save(user);
        return ResponseEntity.status(200).body(new ResponseMessage("Staff added" , staffDTO, true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Long id, @RequestBody UserDTO staffDTO){
        User user = staffRepository.findById(id).orElse(null);
        if (user!= null && user.getStatus().equals("ACTIVE")) {
            user.setFirstname(staffDTO.getFirstname());
            user.setLastname(staffDTO.getLastname());
            user.setPhoneNumber(staffDTO.getPhoneNumber());
            user.setRole(staffDTO.getRole());
            user.setPassword(staffDTO.getPassword());
            staffRepository.save(user);
            return ResponseEntity.status(200).body(new ResponseMessage("Staff updated" , staffDTO, true));
        }
        return ResponseEntity.status(404).body(new ResponseMessage("Staff not found", null, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id){
        Optional<User> byId = staffRepository.findById(id);

        if(byId.isPresent()){
            User user = byId.get();
            user.setStatus("ARXIV");
            return ResponseEntity.status(200).body(new ResponseMessage("Staff deleted" , null, true));
        }

        return ResponseEntity.status(404).body(new ResponseMessage("Staff not found", null, false));

    }


}
