package com.webshop.happy.paws.service;

import com.webshop.happy.paws.data.PetProfileData;
import com.webshop.happy.paws.entity.PetProfile;
import com.webshop.happy.paws.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PetProfileService {

    void addPetProfile(PetProfileData petProfileData, MultipartFile petImage);

    List<PetProfile> getPetProfilesByUser(User user);

    String savePetImage(MultipartFile petImage) throws IOException;


}
