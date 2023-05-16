package subway.application.station;

import subway.application.station.port.in.StationInfoResponseDto;
import subway.domain.station.Station;

public class StationDtoAssembler {

    private StationDtoAssembler() {
    }


    public static StationInfoResponseDto toStationInfoResponseDto(final Station station) {
        return new StationInfoResponseDto(station.getId(), station.getName().getValue());
    }
}
