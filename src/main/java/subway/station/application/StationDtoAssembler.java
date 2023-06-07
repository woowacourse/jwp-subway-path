package subway.station.application;

import subway.station.domain.Station;
import subway.station.dto.response.StationInfoResponse;

public class StationDtoAssembler {

    private StationDtoAssembler() {
    }

    public static StationInfoResponse toStationInfoResponseDto(Station station) {
        return new StationInfoResponse(station.getId(), station.getName().getValue());
    }
}
