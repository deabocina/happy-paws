package com.webshop.happy.paws.data;
import com.webshop.happy.paws.entity.User;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class PetProfileData {
    private User user;
    private MultipartFile petImage;
    private String petName;
    private String petType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
}
