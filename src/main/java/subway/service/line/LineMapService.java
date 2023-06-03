package subway.service.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.LineMap;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.station.LineMapResponse;
import subway.dto.station.StationResponse;
import subway.persistence.repository.section.JdbcSectionRepository;
import subway.persistence.repository.station.JdbcStationRepository;

@Service
public class LineMapService {

    private final JdbcSectionRepository jdbcSectionRepository;
    private final JdbcStationRepository jdbcStationRepository;

    public LineMapService(JdbcSectionRepository jdbcSectionRepository, JdbcStationRepository jdbcStationRepository) {
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
