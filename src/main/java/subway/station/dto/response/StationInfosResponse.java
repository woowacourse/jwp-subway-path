package subway.station.dto.response;

import java.util.List;
import subway.station.dto.request.StationInfoResponseDto;

public class StationInfosResponse {

    private List<StationInfoResponseDto> stations;

    private StationInfosResponse() {
    }

    public StationInfosResponse(List<StationInfoResponseDto> stations) {
        this.stations = stations;
    }

    public List<StationInfoResponseDto> getStations() {
        return stations;
    }
}
