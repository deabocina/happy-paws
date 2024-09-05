package com.webshop.happy.paws.mapper;

import com.webshop.happy.paws.data.PetProfileData;
import com.webshop.happy.paws.entity.PetProfile;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public class PetProfileMapper {
    public static PetProfile mapDataEntity(PetProfileData petProfileData){

        PetProfile petProfile = new PetProfile();
        petProfile.setUser(petProfileData.getUser());
        petProfile.setPetImage(String.valueOf(petProfileData.getPetImage()));
        petProfile.setPetName(petProfileData.getPetName());
        petProfile.setPetType(petProfileData.getPetType());
        petProfile.setBirthDate(petProfileData.getBirthDate());

        return petProfile;

    }

}
