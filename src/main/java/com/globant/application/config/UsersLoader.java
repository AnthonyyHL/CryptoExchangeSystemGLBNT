package com.globant.application.config;

import com.globant.application.port.out.UserRepository;
import com.globant.domain.entities.User;

public class UsersLoader {
    UserRepository userRepository;
    public UsersLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void loadUsers() {
        User user1 = new User("anthleon", "anthleon@gmail.com", "password1");
        User user2 = new User("jose123", "josehc@gmail.com", "12345");
        userRepository.save(user1);
        userRepository.save(user2);
    }
}