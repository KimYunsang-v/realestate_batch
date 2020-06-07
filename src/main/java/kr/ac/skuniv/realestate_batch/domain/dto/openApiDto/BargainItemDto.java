package kr.ac.skuniv.realestate_batch.domain.dto.openApiDto;


import kr.ac.skuniv.realestate_batch.domain.dto.abstractDto.ItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class BargainItemDto extends ItemDto {

    @XmlElement(name = "거래금액")
    private String dealPrice;
}
