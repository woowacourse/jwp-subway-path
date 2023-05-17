package subway.application.service.station;

import org.springframework.stereotype.Service;
import subway.adapter.in.web.station.dto.StationCreateRequest;
import subway.adapter.out.persistence.repository.StationJdbcAdapter;
import subway.application.port.in.station.CreateStationUseCase;
import subway.domain.Station;

import java.util.Optional;

@Service
public class StationCommandService implements CreateStationUseCase {
    private final StationJdbcAdapter stationJdbcAdapter;

    public StationCommandService(final StationJdbcAdapter stationJdbcAdapter) {
        this.stationJdbcAdapter = stationJdbcAdapter;
    }

    public Long createStation(final StationCreateRequest stationCreateRequest) {
        final Station station = new Station(stationCreateRequest.getName());

        final Optional<Station> findByNameStation = stationJdbcAdapter.findByName(station);

        if (findByNameStation.isEmpty()) {
            return stationJdbcAdapter.createStation(station);
        }
        throw new IllegalArgumentException("이미 등록된 역입니다.");
    }
}
