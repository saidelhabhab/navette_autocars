package com.hormattalah.navette_autocars.controller.admin;

import com.hormattalah.navette_autocars.request.UserDto;
import com.hormattalah.navette_autocars.response.SimpleResponse;
import com.hormattalah.navette_autocars.service.admin.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/admin/")
@RequiredArgsConstructor
public class AdminController {


    private final UsersService usersService;


    @GetMapping("users")
    public ResponseEntity<Page<UserDto>> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserDto> users = usersService.getAllUser(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(usersService.getUserById(userId));
    }

    @GetMapping("userBy/{userSecondId}")
    public ResponseEntity<UserDto> getUserBysSecondId(@PathVariable Long userSecondId){
        return ResponseEntity.ok(usersService.getUserById(userSecondId));
    }

    @PutMapping("updateUser/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @ModelAttribute UserDto userDto) throws IOException {
        UserDto updated = usersService.updateUser(userId,userDto);

        if(updated != null){
            return  ResponseEntity.ok(updated);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("users/count")
    public ResponseEntity<Long> countUsers() {
        return ResponseEntity.ok(usersService.countUsers());
    }


    @GetMapping("user/instructor/{userId}/{status}")
    public ResponseEntity<Object> changeStatus(@PathVariable Long userId, @PathVariable String status) {
        UserDto userDto = usersService.changeStatus(userId, status);
        if (userDto == null) {
            return new ResponseEntity<>(new SimpleResponse("Something went wrong!"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }


}