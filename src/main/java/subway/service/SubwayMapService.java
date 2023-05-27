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
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
public class SubwayMapService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SubwayMapService(final SectionRepository sectionRepository, final StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public LineMapResponse findById(final Long id) {
        Sections sections = sectionRepository.findById(id);
        LineMap lineMap = new LineMap(sections);

        List<Station> orderedStations = lineMap.getOrderedStations();
        List<StationResponse> stations = orderedStations.stream()
                .map(station -> StationResponse.from(stationRepository.findByName(station.getName()).getId(), station))
                .collect(Collectors.toList());

        return LineMapResponse.from(stations);
    }
}
