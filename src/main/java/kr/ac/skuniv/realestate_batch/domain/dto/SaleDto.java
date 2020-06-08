package kr.ac.skuniv.realestate_batch.domain.dto;

import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.SaleBodyDto;
import kr.ac.skuniv.realestate_batch.domain.dto.abstractDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.HeaderDto;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@ToString
@Getter
@XmlRootElement(name = "response")
public class SaleDto extends BuildingDealDto {
    @XmlElement(name = "body")
    private SaleBodyDto body;

    @XmlElement(name = "header")
    private HeaderDto header;
}
