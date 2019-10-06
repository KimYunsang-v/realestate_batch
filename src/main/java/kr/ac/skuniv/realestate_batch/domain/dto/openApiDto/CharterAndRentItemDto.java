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

    @XmlElements({
        @XmlElement(name = "보증금액"),
        @XmlElement(name = "보증금")
    })
    private String guaranteePrice;

    @XmlElements({
            @XmlElement(name = "월세금액"),
            @XmlElement(name = "월세")
    })
    private String monthlyPrice;
}
