package com.librarymanagement.service;

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.entity.User;
import com.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        userRepository.save(user);
        return "User registered successfully!";
    }
}
