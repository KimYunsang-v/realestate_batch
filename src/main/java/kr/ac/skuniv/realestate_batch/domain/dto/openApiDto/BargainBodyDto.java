package kr.ac.skuniv.realestate_batch.domain.dto.openApiDto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class BargainBodyDto {

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<BargainItemDto> item;

    @XmlElement(name = "numOfRows")
    private int numOfRows;

    @XmlElement(name = "pageNo")
    private int pageNo;

    @XmlElement(name = "totalCount")
    private int totalCount;
}
