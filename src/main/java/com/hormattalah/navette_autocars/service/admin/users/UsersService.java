package com.hormattalah.navette_autocars.service.admin.users;

import com.hormattalah.navette_autocars.request.UserDto;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UsersService {

    public Page<UserDto> getAllUser(int page, int size);

    public UserDto getUserById(Long userId);

    // In UserService.java
    public long countUsers();


    public  UserDto updateUser(Long userId,UserDto userDto) throws IOException;

    public UserDto changeStatus(Long userId, String status);



}