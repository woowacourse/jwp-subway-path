package subway.application.service.station;

import org.springframework.stereotype.Service;
import subway.adapter.in.web.station.dto.StationCreateRequest;
import subway.application.port.in.station.CreateStationUseCase;
import subway.application.port.out.station.StationCommandPort;
import subway.application.port.out.station.StationQueryPort;
import subway.domain.Station;

import java.util.Optional;

@Service
public class StationCommandService implements CreateStationUseCase {

    private final StationCommandPort stationCommandPort;
    private final StationQueryPort stationQueryPort;

    public StationCommandService(final StationCommandPort stationCommandPort, final StationQueryPort stationQueryPort) {
        this.stationCommandPort = stationCommandPort;
        this.stationQueryPort = stationQueryPort;
    }

    public Long createStation(final StationCreateRequest stationCreateRequest) {
        final Station station = new Station(stationCreateRequest.getName());

        final Optional<Station> findByNameStation = stationQueryPort.findByName(station);

        if (findByNameStation.isEmpty()) {
            return stationCommandPort.createStation(station);
        }
        throw new IllegalArgumentException("이미 등록된 역입니다.");
    }
}
