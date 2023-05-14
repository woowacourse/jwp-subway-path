package subway.subwayMap.service;

import org.springframework.stereotype.Service;
import subway.line.domain.Line;
import subway.line.dto.LineResponse;
import subway.line.service.LineService;
import subway.section.domain.Section;
import subway.section.service.SectionService;
import subway.station.domain.Station;
import subway.subwayMap.domain.SectionToStationConverter;
import subway.subwayMap.domain.SubwayMap;
import subway.subwayMap.dto.SubwayMapForLineResponse;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayMapService {

    private final LineService lineService;
    private final SectionService sectionService;
    private final SubwayMap subwayMap;

    public SubwayMapService(final LineService lineService, final SectionService sectionService, final SubwayMap subwayMap) {
        this.lineService = lineService;
        this.sectionService = sectionService;
        this.subwayMap = subwayMap;
    }

    public List<SubwayMapForLineResponse> findAllSubwayMap() {
        final List<Line> lines = lineService.findAllLine();
        return lines.stream()
                .map(this::findSubwayMapByLineId)
                .collect(Collectors.toList());
    }

    public SubwayMapForLineResponse findSubwayMapByLineId(final Line line) {
        return new SubwayMapForLineResponse(new LineResponse(line.getId(), line.getName(), line.getColor()), subwayMap.getSubwayMapByLine(line));
    }

    public SubwayMapForLineResponse findSubwayMapByLineId(final Long id) {
        Line line = lineService.findLineById(id);
        return new SubwayMapForLineResponse(new LineResponse(line.getId(), line.getName(), line.getColor()), subwayMap.getSubwayMapByLine(line));
    }

    @PostConstruct
    private void initialize() {
        final List<Line> lines = lineService.findAllLine();
        for (Line line : lines) {
            final List<Section> sections = sectionService.findSectionsByLineId(line.getId());
            final SectionToStationConverter sectionToStationConverter = SectionToStationConverter.of(sections);
            final List<Station> stations = sectionToStationConverter.getSortedStation();
            subwayMap.put(line, stations);
        }
    }
}
