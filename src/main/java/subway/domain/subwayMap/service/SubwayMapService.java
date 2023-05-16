package subway.domain.subwayMap.service;

import org.springframework.stereotype.Service;
import subway.domain.station.domain.Station;
import subway.domain.station.service.StationService;
import subway.domain.subwayMap.domain.SectionToStationConverter;
import subway.domain.subwayMap.dto.SubwayMapForLineResponse;
import subway.domain.line.domain.Line;
import subway.domain.line.dto.LineResponse;
import subway.domain.line.service.LineService;
import subway.domain.section.domain.Section;
import subway.domain.section.service.CreateSectionService;
import subway.domain.subwayMap.domain.SubwayMap;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayMapService {

    private final LineService lineService;
    private final StationService stationService;
    private final CreateSectionService createSectionService;
    private final SubwayMap subwayMap;

    public SubwayMapService(final LineService lineService, final StationService stationService, final CreateSectionService createSectionService, final SubwayMap subwayMap) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.createSectionService = createSectionService;
        this.subwayMap = subwayMap;
    }

    public void addStation(Long lineId,Long baseStationId, Long addStationId ,boolean direction) {
        subwayMap.addStation(lineService.findLineById(lineId),
                stationService.findStationById(baseStationId),
                stationService.findStationById(addStationId),
                direction);
    }

    public void deleteStation(Long lineId,Long stationId) {
        subwayMap.deleteStation(lineService.findLineById(lineId),
                stationService.findStationById(stationId));
    }

    public List<SubwayMapForLineResponse> findAllSubwayMap() {
        final List<Line> lines = lineService.findAllLine();
        return lines.stream()
                .map(this::findSubwayMapByLine)
                .collect(Collectors.toList());
    }

    private SubwayMapForLineResponse findSubwayMapByLine(final Line line) {
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
            final List<Section> sections = createSectionService.findSectionsByLineId(line.getId());
            final SectionToStationConverter sectionToStationConverter = SectionToStationConverter.of(sections);
            final List<Station> stations = sectionToStationConverter.getSortedStation();
            subwayMap.put(line, stations);
        }
    }
}
