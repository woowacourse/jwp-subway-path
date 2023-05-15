package subway.station.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import subway.section.domain.Direction;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class StationSaveRequest {
    private final Long lineId;
    private final String baseStation;
    private final Direction direction;
    private final String additionalStation;
    private final Long distance;
}
