package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.Station;

import java.util.List;

@Getter
@AllArgsConstructor
public class LineStationResponse {
    private final Long lineId;
    private final List<Station> stations;
}
