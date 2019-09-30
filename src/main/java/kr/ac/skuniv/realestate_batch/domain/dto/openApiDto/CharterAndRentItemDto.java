package kr.ac.skuniv.realestate_batch.domain.dto.openApiDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class CharterAndRentItemDto extends ItemDto{

    @XmlElement(name = "보증금액")
    private String guaranteePrice;

    @XmlElement(name = "월세금액")
    private String monthlyPrice;


}
