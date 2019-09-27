package kr.ac.skuniv.realestate_batch.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Building {

    @Id
    @GeneratedValue
    private Long id;

    private int city;
    private int groop;
    private String dong;
    private String name;
    private Double area;
    private int floor;
    private int type;
    private String constructYear;
    private String latitude;
    private String longitude;

    @Column(name = "building_num")
    private String buildingNum;
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<BargainDate> bargainDates = new HashSet<BargainDate>();

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<CharterDate> charterDates = new HashSet<CharterDate>();

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<RentDate> rentDates = new HashSet<RentDate>();

}
