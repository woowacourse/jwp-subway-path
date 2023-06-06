package subway.station.application;

import subway.station.domain.Station;
import subway.station.dto.request.StationInfoResponseDto;

public class StationDtoAssembler {

    private StationDtoAssembler() {
    }


    public static StationInfoResponseDto toStationInfoResponseDto(Station station) {
        return new StationInfoResponseDto(station.getId(), station.getName().getValue());
    }
}
