package kr.ac.skuniv.realestate_batch.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BargainDate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "building_id")
    private BuildingEntity buildingEntity;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String price;
}
