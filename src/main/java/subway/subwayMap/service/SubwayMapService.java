package subway.subwayMap.service;

import org.springframework.stereotype.Service;
import subway.line.domain.Line;
import subway.line.service.LineService;
import subway.subwayMap.domain.SubwayMap;
import subway.section.domain.Section;
import subway.section.service.SectionService;
import subway.station.domain.Station;
import subway.subwayMap.dto.SubwayMapResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayMapService {

    private final LineService lineService;
    private final SectionService sectionService;

    public SubwayMapService(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    public List<SubwayMapResponse> findAllSubwayMap() {
        final List<Line> lines = lineService.findAllLine();
        return lines.stream()
                .map(Line::getId)
                .map(this::findSubwayMapByLineId)
                .collect(Collectors.toList());
    }

    public SubwayMapResponse findSubwayMapByLineId(final Long id) {
        final List<Section> sections = sectionService.findSectionsByLineId(id);
        final SubwayMap subwayMap = SubwayMap.of(sections);
        final List<Station> stations = subwayMap.getStations();
        final Line line = lineService.findLineById(id);
        return new SubwayMapResponse(line.getId(), line.getName(), line.getColor(), stations);
    }
}
