package kr.ac.skuniv.realestate_batch.domain.dto.openApiDto;


import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@ToString
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemDto {

    @XmlElement(name = "건축년도")
    private int constructYear;

    @XmlElement(name = "년")
    private int year;

    @XmlElement(name = "법정동")
    private String dong;

    @XmlElements({
            @XmlElement(name = "아파트"),
            @XmlElement(name = "연립다세대"),
            @XmlElement(name = "단지")
    })
    private String name;

    @XmlElement(name = "월")
    private int monthly;

    @XmlElement(name = "일")
    private String days;


    @XmlElements({
            @XmlElement(name = "전용면적"),
            @XmlElement(name = "대지면적"),
            @XmlElement(name = "계약면적"),
    })
    private Double area;

    @XmlElement(name = "지번")
    private String buildingNum;

    @XmlElement(name = "지역코드")
    private String regionCode;

    @XmlElement(name = "층")
    private int floor;
}
