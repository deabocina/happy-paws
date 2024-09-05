package com.webshop.happy.paws.service;

import com.webshop.happy.paws.data.UserData;
import com.webshop.happy.paws.entity.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    boolean inputValidation(UserData request);

    User register(UserData request);

    User authentication(String email, String inpPassword);

    User getUserById(UUID userId);

    List<User> getAllUsers();

}
