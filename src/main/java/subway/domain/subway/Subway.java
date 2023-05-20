package subway.domain.subway;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import subway.domain.exception.DomainException;
import subway.domain.exception.ExceptionType;

public class Subway {
    private final Map<Line, List<Station>> subway;

    public Subway(List<Line> lines, List<Station> stations, List<Section> allSections) {
        this.subway = makeSubway(lines, stations, allSections);
    }

    private Map<Line, List<Station>> makeSubway(List<Line> lines, List<Station> stations, List<Section> allSections) {
        return lines.stream()
            .collect(Collectors.toMap(
                line -> line,
                line -> getStations(line, stations, allSections)));
    }

    private List<Station> getStations(Line line, List<Station> stations, List<Section> allSections) {
        List<Section> sections = allSections.stream()
            .filter(section -> section.getLineId().equals(line.getId()))
            .collect(Collectors.toUnmodifiableList());

        Sections sectionsInThisLine = new Sections(sections);

        return sectionsInThisLine.findOrderedStationIds().stream()
            .map(stationId -> mapStationIdToStation(stationId, stations))
            .collect(Collectors.toUnmodifiableList());
    }

    private Station mapStationIdToStation(Long stationId, List<Station> stations) {
        return stations.stream()
            .filter(station -> station.getId().equals(stationId))
            .findFirst()
            .orElseThrow(() -> new DomainException(ExceptionType.NO_EXISTENT_STATION));
    }

    public Map<Line, List<Station>> getLineMap() {
        return subway;
    }
}
