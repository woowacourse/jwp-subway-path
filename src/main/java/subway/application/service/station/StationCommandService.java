package subway.application.service.station;

import org.springframework.stereotype.Service;
import subway.adapter.in.web.station.dto.StationCreateRequest;
import subway.application.port.in.station.CreateStationUseCase;
import subway.application.port.out.station.StationCommandHandler;
import subway.application.port.out.station.StationQueryHandler;
import subway.domain.Station;

import java.util.Optional;

@Service
public class StationCommandService implements CreateStationUseCase {

    private final StationCommandHandler stationCommandPort;
    private final StationQueryHandler stationQueryPort;

    public StationCommandService(final StationCommandHandler stationCommandPort, final StationQueryHandler stationQueryPort) {
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
