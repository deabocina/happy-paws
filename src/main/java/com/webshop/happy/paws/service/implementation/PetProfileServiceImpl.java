package com.webshop.happy.paws.service.implementation;

import com.webshop.happy.paws.data.PetProfileData;
import com.webshop.happy.paws.entity.PetProfile;
import com.webshop.happy.paws.entity.User;
import com.webshop.happy.paws.mapper.PetProfileMapper;
import com.webshop.happy.paws.repository.PetProfileRepository;
import com.webshop.happy.paws.service.PetProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class PetProfileServiceImpl implements PetProfileService {

    private final PetProfileRepository petProfileRepository;

    @Override
    public String savePetImage(MultipartFile petImage) throws IOException {
        String fileName = StringUtils.cleanPath(petImage.getOriginalFilename());
        String uploadDir = "src/main/resources/static/img";

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File destFile = new File(uploadPath.getAbsolutePath() + File.separator + fileName);

        try (OutputStream os = new FileOutputStream(destFile)) {
            os.write(petImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return fileName;
    }

    @Override
    public void addPetProfile(PetProfileData petProfileData, MultipartFile petImage) {
        try {
            PetProfile petProfile = PetProfileMapper.mapDataEntity(petProfileData);

            if (petImage != null && !petImage.isEmpty()) {
                String petImageName = savePetImage(petImage);
                petProfile.setPetImage(petImageName);
            }

            petProfileRepository.save(petProfile);
        } catch (IOException e) {
            throw new RuntimeException("Error saving pet image", e);
        }
    }


    @Override
    public List<PetProfile> getPetProfilesByUser(User user) {
        return petProfileRepository.findByUser(user);
    }
}
