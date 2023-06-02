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

@Service
public class SubwayMapService {

    private final SectionRepository sectionRepository;

    public SubwayMapService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public LineMapResponse findById(final Long id) {
        Sections sections = sectionRepository.findByLineId(id);
        LineMap lineMap = new LineMap(sections);

        List<Station> orderedStations = lineMap.getOrderedStations();
        List<StationResponse> stations = orderedStations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return LineMapResponse.from(stations);
    }
}
