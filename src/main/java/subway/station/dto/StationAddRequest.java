package subway.station.dto;

import lombok.*;
import subway.section.domain.Direction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StationAddRequest {
    @NotNull(message = "lineId는 null일 수 없습니다.")
    private Long lineId;
    @NotBlank(message = "기준역은 null 또는 empty일 수 없습니다.")
    private String baseStation;
    @NotNull(message = "방향은 LEFT 또는 RIGHT만 입력할 수 있습니다.")
    private Direction direction;
    @NotBlank(message = "등록할 역은 null 또는 empty일 수 없습니다.")
    private String additionalStation;
    @NotNull(message = "거리는 null일 수 없습니다.")
    private Long distance;
}
