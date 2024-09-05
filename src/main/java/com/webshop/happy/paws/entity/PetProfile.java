package com.webshop.happy.paws.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "pet_profile", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "pet_image")
    private String petImage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "pet_name", length = 30)
    private String petName;

    @Column(name = "pet_type", length = 20)
    private String petType;

    @Setter
    @Getter
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

}
