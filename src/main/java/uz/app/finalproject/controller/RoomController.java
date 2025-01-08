package uz.app.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.finalproject.dto.RoomDTO;
import uz.app.finalproject.entity.ResponseMessage;
import uz.app.finalproject.entity.Room;
import uz.app.finalproject.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    final RoomRepository repository;

    @GetMapping
    public ResponseEntity<?> getRooms() {
        return ResponseEntity.status(200).body(new ResponseMessage("All rooms" , repository.findAll() , true));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable String id) {
        repository.deleteById(Long.valueOf(id));
        return ResponseEntity.status(200).body(new ResponseMessage("Room deleted" , null , true));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id , RoomDTO roomDTO){
        Optional<Room> byId = repository.findById(Long.valueOf(id));
        if (byId.isPresent()) {
            Room room = byId.get();
            room.setRoomName(roomDTO.getRoomName());
            room.setCapacity(roomDTO.getCapacity());
            room.setCountOfTable(roomDTO.getCountOfTable());
            room.setCountOfChair(roomDTO.getCountOfChair());
            repository.save(room);
            return ResponseEntity.status(200).body(new ResponseMessage("Room updated" , roomDTO , true));
        }
        return ResponseEntity.status(409).body(new ResponseMessage("Room not founded" , null , false));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoomDTO roomDTO){
        Room room = new Room();
        room.setRoomName(roomDTO.getRoomName());
        room.setCapacity(roomDTO.getCapacity());
        room.setCountOfTable(roomDTO.getCountOfTable());
        room.setCountOfChair(roomDTO.getCountOfChair());
        repository.save(room);
        return ResponseEntity.status(200).body(new ResponseMessage("Room created" , roomDTO , true));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String search){
        List<Room> allByRoomNameContains = repository.findAllByRoomNameContains(search);
        return ResponseEntity.status(200).body(new ResponseMessage("Result" , allByRoomNameContains, true));
    }

}
