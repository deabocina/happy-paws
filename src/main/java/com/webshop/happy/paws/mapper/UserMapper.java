package com.webshop.happy.paws.mapper;

import com.webshop.happy.paws.data.UserData;
import com.webshop.happy.paws.entity.User;
import com.webshop.happy.paws.utils.PasswordUtils;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public class UserMapper {
    public static User mapDataEntity(UserData userData){

        User user = new User();
        user.setName(userData.getName());
        user.setSurname(userData.getSurname());
        user.setEmail(userData.getEmail());
        user.setPassword(userData.getPassword());
        user.setCity(userData.getCity());
        user.setAddress(userData.getAddress());
        user.setGender(userData.getGender());
        user.setRole(userData.getRole());
        user.setPassword(PasswordUtils.hashPassword(userData.getPassword()));
        user.setPhoneNumber(userData.getPhoneNumber());
        user.setDateOfBirth(userData.getDateOfBirth());

        return user;

    }

}
