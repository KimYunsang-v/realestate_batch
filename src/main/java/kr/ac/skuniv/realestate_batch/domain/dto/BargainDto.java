package kr.ac.skuniv.realestate_batch.domain.dto;

import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainBodyDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.HeaderDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@ToString
@Getter
@XmlRootElement(name = "response")
public class BargainDto extends BuildingDealDto {
    @XmlElement(name = "body")
    private BargainBodyDto body;

    @XmlElement(name = "header")
    private HeaderDto header;
}
