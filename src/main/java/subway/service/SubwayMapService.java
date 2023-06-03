package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.LineMap;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.station.LineMapResponse;
import subway.dto.station.StationResponse;
import subway.repository.JdbcSectionRepository;
import subway.repository.JdbcStationRepository;

@Service
public class SubwayMapService {

    private final JdbcSectionRepository jdbcSectionRepository;
    private final JdbcStationRepository jdbcStationRepository;

    public SubwayMapService(JdbcSectionRepository jdbcSectionRepository, JdbcStationRepository jdbcStationRepository) {
        this.jdbcSectionRepository = jdbcSectionRepository;
        this.jdbcStationRepository = jdbcStationRepository;
    }

    @Transactional(readOnly = true)
    public LineMapResponse findById(final Long id) {
        Sections sections = jdbcSectionRepository.findByLineId(id);
        LineMap lineMap = new LineMap(sections);

        List<Station> orderedStations = lineMap.getOrderedStations();
        List<StationResponse> stations = orderedStations.stream()
                .map(station -> jdbcStationRepository.findByName(station.getName()))
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return LineMapResponse.from(stations);
    }
}
