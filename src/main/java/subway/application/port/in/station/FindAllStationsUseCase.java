package subway.application.port.in.station;

import java.util.List;
import subway.application.port.in.station.dto.response.StationQueryResponse;

public interface FindAllStationsUseCase {

    List<StationQueryResponse> findAllStations();
}
