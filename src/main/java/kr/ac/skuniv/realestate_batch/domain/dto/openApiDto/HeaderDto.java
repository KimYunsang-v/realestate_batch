package kr.ac.skuniv.realestate_batch.domain.dto.openApiDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@NoArgsConstructor
@XmlRootElement(name = "header")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeaderDto {
    @XmlElement(name = "resultCode")
    private int resultCode;

    @XmlElement(name = "resultMsg")
    private String resultMsg;
}
