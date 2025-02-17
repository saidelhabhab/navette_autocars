package com.hormattalah.navette_autocars.service;


import com.hormattalah.navette_autocars.entity.User;
import com.hormattalah.navette_autocars.repository.UserRepo;
import com.hormattalah.navette_autocars.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private  UserRepo userRepo;

    @Autowired
    private  JwtUtil jwtUtil;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepo.findByEmail(username);

        if (user.isEmpty()) throw new UsernameNotFoundException("Email is not valid",null);

        return new org.springframework.security.core.userdetails.User(user.get().getEmail(),user.get().getPassword(),new ArrayList<>());

    }


}