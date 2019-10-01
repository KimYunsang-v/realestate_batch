package kr.ac.skuniv.realestate_batch.domain.dto.openApiDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NaverLocationDto {
    @NonNull
    @JsonProperty("y")
    private String latitude;
    @NonNull
    @JsonProperty("x")
    private String longitude;

    @Builder
    public NaverLocationDto(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
