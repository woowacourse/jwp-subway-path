package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.station.StationRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.repository.JdbcStationRepository;

@Transactional
@Service
public class StationService {

    private final JdbcStationRepository jdbcStationRepository;

    public StationService(final JdbcStationRepository jdbcStationRepository) {
        this.jdbcStationRepository = jdbcStationRepository;
    }

    public Long save(final StationRequest stationRequest) {
        return jdbcStationRepository.save(new Station(stationRequest.getName()));
    }

    public void removeById(final Long id) {
        jdbcStationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public StationsResponse findAll() {
        List<StationResponse> stations = jdbcStationRepository.findAll().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return StationsResponse.from(stations);
    }

    @Transactional(readOnly = true)
    public StationResponse findById(final Long id) {
        Station station = jdbcStationRepository.findById(id);
        return StationResponse.from(station);
    }
}
