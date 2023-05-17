package subway.application.port.in.station;

import subway.adapter.in.web.station.dto.StationCreateRequest;

public interface CreateStationUseCase {
    Long createStation(final StationCreateRequest stationCreateRequest);
}
