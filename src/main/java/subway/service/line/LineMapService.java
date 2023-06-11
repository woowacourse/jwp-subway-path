package subway.service.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.LineMap;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.dto.station.LineMapResponse;
import subway.dto.station.StationResponse;

@Service
public class LineMapService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public LineMapService(final SectionRepository sectionRepository, final StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public LineMapResponse findById(final Long id) {
        Sections sections = sectionRepository.findByLineId(id);
        LineMap lineMap = new LineMap(sections);

        List<Station> orderedStations = lineMap.getOrderedStations();
        List<StationResponse> stations = orderedStations.stream()
                .map(station -> stationRepository.findByName(station.getName()))
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return LineMapResponse.from(stations);
    }
}
