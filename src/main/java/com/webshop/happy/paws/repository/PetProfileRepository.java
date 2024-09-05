package com.webshop.happy.paws.repository;

import com.webshop.happy.paws.entity.PetProfile;
import com.webshop.happy.paws.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetProfileRepository extends JpaRepository<PetProfile, UUID> {

    List<PetProfile> findByUser(User user);

}
