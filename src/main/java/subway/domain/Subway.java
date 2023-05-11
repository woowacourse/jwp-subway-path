package subway.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import subway.exception.DomainException;
import subway.exception.ExceptionType;

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
        List<Long> stationIds = allSections.stream()
            .filter(section -> section.getLineId().equals(line.getId()))
            .flatMap(section -> Stream.of(section.getSourceStationId(), section.getTargetStationId()))
            .distinct()
            .collect(Collectors.toList());

        return stationIds.stream()
            .map(stationId -> mapStationIdToStation(stationId, stations))
            .collect(Collectors.toList());
    }

    private Station mapStationIdToStation(Long stationId, List<Station> stations) {
        return stations.stream()
            .filter(station -> station.getId().equals(stationId))
            .findFirst()
            .orElseThrow(() -> new DomainException(ExceptionType.NO_EXISTENT_STATION));
    }

    public Map<Line, List<Station>> getSubway() {
        return subway;
    }
}
