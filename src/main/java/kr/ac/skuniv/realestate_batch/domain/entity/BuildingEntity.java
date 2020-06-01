package kr.ac.skuniv.realestate_batch.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingEntity {

    @Id
    @GeneratedValue
    private Long id;

    private int city;
    private int groop;
    private String dong;
    private String name;
    private Double area;
    private int floor;
    private String type;
    private String constructYear;
    private String latitude;
    private String longitude;
    private String buildingNum;

    @OneToMany(mappedBy = "buildingEntity", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnore
    private Set<BargainDate> bargainDates = new HashSet<BargainDate>();

    @OneToMany(mappedBy = "buildingEntity", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnore
    private Set<CharterDate> charterDates = new HashSet<CharterDate>();

    @OneToMany(mappedBy = "buildingEntity", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnore
    private Set<RentDate> rentDates = new HashSet<RentDate>();
}
