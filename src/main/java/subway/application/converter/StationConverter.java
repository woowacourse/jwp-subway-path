package subway.application.converter;

import subway.application.domain.Station;
import subway.ui.dto.response.StationResponse;

public class StationConverter {

    public static StationResponse domainToResponseDto(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
