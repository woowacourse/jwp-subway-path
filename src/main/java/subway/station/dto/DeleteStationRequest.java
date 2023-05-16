package subway.station.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DeleteStationRequest {
    @NotNull(message = "lineId는 null일 수 없습니다.")
    private Long lineId;
    @NotNull(message = "stationId는 null일 수 없습니다.")
    private Long stationId;
}
