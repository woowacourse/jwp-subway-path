package subway.station.application;

import subway.station.application.dto.request.StationInfoResponseDto;
import subway.station.domain.Station;

public class StationDtoAssembler {

    private StationDtoAssembler() {
    }


    public static StationInfoResponseDto toStationInfoResponseDto(Station station) {
        return new StationInfoResponseDto(station.getId(), station.getName().getValue());
    }
}
