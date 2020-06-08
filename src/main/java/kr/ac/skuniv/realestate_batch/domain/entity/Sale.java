package kr.ac.skuniv.realestate_batch.domain.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int city;
	private int groop;
	private String dong;
	private String name;
	private Double area;
	private String type;

	@Temporal(TemporalType.DATE)
	private Date date;

	private String price;

	private Double pyPrice;
}
