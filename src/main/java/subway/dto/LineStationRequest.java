package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LineStationRequest {
    @NotNull
    @Min(value = 0, message = "{value} 이상의 번호를 입력해주세요")
    private Long preStationId;
    @NotNull
    @Min(value = 0, message = "{value} 이상의 번호를 입력해주세요")
    private Long stationId;
    @NotNull
    @Min(value = 0, message = "{value} 이상의 거리를 입력해주세요")
    private Long distance;
}
