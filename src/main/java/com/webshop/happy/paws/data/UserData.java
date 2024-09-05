package com.webshop.happy.paws.data;

import lombok.Data;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UserData {

    private String role;
    private String name;
    private String surname;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private String password;
    private String city;
    private String address;

}
