package subway.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.station.Station;

@Getter
@AllArgsConstructor
public class StationResponse {

    private final Long id;
    private final String name;

    public static StationResponse from(final Station station) {
        return new StationResponse(station.getId(), station.getName().getValue());
    }
}
