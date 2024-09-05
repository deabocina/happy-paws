package com.webshop.happy.paws.service.implementation;

import com.webshop.happy.paws.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.webshop.happy.paws.service.UserService;
import com.webshop.happy.paws.data.UserData;
import com.webshop.happy.paws.mapper.UserMapper;
import com.webshop.happy.paws.entity.User;
import com.webshop.happy.paws.utils.PasswordUtils;
import com.webshop.happy.paws.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean inputValidation(UserData request){
        return ValidationUtils.isStringNullOrEmpty(request.getName())||
                ValidationUtils.isStringNullOrEmpty(request.getSurname())||
                ValidationUtils.isStringNullOrEmpty(request.getName())||
                ValidationUtils.isStringNullOrEmpty(request.getCity())||
                ValidationUtils.isStringNullOrEmpty(request.getEmail())||
                ValidationUtils.isStringNullOrEmpty(request.getAddress())||
                ValidationUtils.isStringNullOrEmpty(request.getGender())||
                ValidationUtils.isAgeInvalid(request.getDateOfBirth())||
                ValidationUtils.isStringNullOrEmpty(request.getPassword());
    }

    @Override
    public User register(UserData request) {
        if (inputValidation(request)) {
            throw new RuntimeException("Invalid user input");
        }
        else if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Duplicate email");
        }

        User user = UserMapper.mapDataEntity(request);
        return userRepository.save(user);
    }

    @Override
    public User authentication(String email, String inpPassword){
        return userRepository.findByEmail(email)
                .map(user -> {
                    if (PasswordUtils.verifyPassword(inpPassword, user.getPassword())) {
                        return user;
                    }
                    else {
                        throw new RuntimeException("Wrong password");
                    }
                })
                .orElseThrow(() -> new RuntimeException("Authentication failed"));
    }

    @Override
    public User getUserById(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
