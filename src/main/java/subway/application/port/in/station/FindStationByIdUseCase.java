package subway.application.port.in.station;

import subway.application.port.in.station.dto.response.StationQueryResponse;

public interface FindStationByIdUseCase {

    StationQueryResponse findStationById(long stationId);
}
