package kr.ac.skuniv.realestate_batch.domain.dto.abstractDto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class BuildingDealDto {
    private String buildingType;
    private String dealType;
    private String apiKind;
    private Date date;
}
