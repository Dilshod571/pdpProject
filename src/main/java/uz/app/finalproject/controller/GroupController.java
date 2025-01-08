package uz.app.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.finalproject.dto.GroupDTO;
import uz.app.finalproject.entity.Groups;
import uz.app.finalproject.entity.ResponseMessage;
import uz.app.finalproject.entity.Room;
import uz.app.finalproject.entity.User;
import uz.app.finalproject.repository.GroupRepository;
import uz.app.finalproject.repository.RoomRepository;
import uz.app.finalproject.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    final GroupRepository groupRepository;
    final UserRepository userRepository;
    final RoomRepository repository;
    private final RoomRepository roomRepository;

    @GetMapping("/active")
    public ResponseEntity<?> groupsActive() {

        Optional<Groups> active = groupRepository.findByStatus("ACTIVE");
        if (active.isPresent()) {
            List<Groups> group = (List<Groups>) active.get();
            return ResponseEntity.ok(new ResponseMessage("All active groups", group, true));
        }
        return ResponseEntity.status(409).body(new ResponseMessage("Active groups not found", null, false));
    }

    @GetMapping("/notActive")
    public ResponseEntity<?> groupsArxiv() {

        Optional<Groups> active = groupRepository.findByStatus("ARXIV");
        if (active.isPresent()) {
            List<Groups> group = (List<Groups>) active.get();
            return ResponseEntity.ok(new ResponseMessage("Not active groups", group, true));
        }
        return ResponseEntity.status(409).body(new ResponseMessage("Not active groups not found", null, false));
    }


    @PostMapping("/addGroup")
    public ResponseEntity<?> addGroup(@RequestBody GroupDTO groupDTO) {

        if ( groupRepository.existsByGroupNameAndStatusNot(groupDTO.getGroupName() , "ARXIV") ){
            return ResponseEntity.status(409).body(new ResponseMessage("Group with this name already exists", null, false));
        }

        User teacher = userRepository.findUserByFirstnameAndLastname(groupDTO.getTeacher().getFirstname(), groupDTO.getTeacher().getLastname());
        Room byRoomName = roomRepository.findByRoomName(groupDTO.getRoom());

        if ( teacher.getRole().equals("TEACHER") ){

            Groups groups = new Groups();
            groups.setGroupName(groupDTO.getGroupName());
            groups.setDays(groupDTO.getDays());
            groups.setStatus("ACTIVE");
            groups.setStartTime(groupDTO.getStartTime());
            groups.setTeacher(teacher);
            groups.setRoom(byRoomName);
            groupRepository.save(groups);
            return ResponseEntity.status(200).body(new ResponseMessage("Group added", groupDTO, true));
        }


        return ResponseEntity.status(409).body(new ResponseMessage("This teacher not found by these firstname and lastname", null, false));

    }

    @PostMapping("/search")
    public ResponseEntity<?> searchGroup(@RequestParam String search) {

        List<Groups> byGroupNameContains = groupRepository.findAllByGroupNameContainsAndStatusEquals(search , "ACTIVE");
        return ResponseEntity.status(200).body(new ResponseMessage("Result", byGroupNameContains, true));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGroup(@RequestBody GroupDTO groupDTO, @PathVariable String id) {

        Optional<Groups> byId = groupRepository.findById(Long.valueOf(id));

        if ( byId.isPresent() ){
            Groups groups = byId.get();

            groups.setGroupName(groupDTO.getGroupName());
            groups.setDays(groupDTO.getDays());
            groups.setStatus("ACTIVE");
            groups.setStartTime(groupDTO.getStartTime());
            groups.setTeacher(userRepository.findUserByFirstnameAndLastname(groupDTO.getTeacher().getFirstname(), groupDTO.getTeacher().getLastname()));
            groups.setRoom(roomRepository.findByRoomName(groupDTO.getRoom()));
            groupRepository.save(groups);
            return ResponseEntity.status(200).body(new ResponseMessage("Group updated", groupDTO, true));

        }

        return ResponseEntity.status(409).body(new ResponseMessage("Group not found", null, false));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable String id) {
        Optional<Groups> byId = groupRepository.findById(Long.valueOf(id));

        if ( byId.isPresent() ){
            Groups groups = byId.get();

            if ( !groups.getStatus().equals("ARXIV") ) {

                groups.setStatus("ARXIV");
                groupRepository.save(groups);
                return ResponseEntity.status(200).body(new ResponseMessage("Group deleted", null, true));
            }
            return ResponseEntity.status(409).body(new ResponseMessage("Group can't be deleted because this group already deleted!", null, false));
        }

        return ResponseEntity.status(409).body(new ResponseMessage("Group not found", null, false));
    }



}
