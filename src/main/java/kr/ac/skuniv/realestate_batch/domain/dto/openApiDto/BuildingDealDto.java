package kr.ac.skuniv.realestate_batch.domain.dto.openApiDto;

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
}