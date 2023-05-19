package subway.shortestpathfinder.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GetShortestPathRequest {
    @NotBlank(message = "startStationName은 null 또는 empty일 수 없습니다.")
    private String startStationName;
    @NotBlank(message = "endStationName은 null 또는 empty일 수 없습니다.")
    private String endStationName;
}
