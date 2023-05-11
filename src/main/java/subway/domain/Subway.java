package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import subway.exception.DomainException;
import subway.exception.ExceptionType;

public class Subway {
    private final Map<Line, List<Station>> subway;

    public Subway(List<Line> lines, List<Station> stations, List<Section> allSections) {
        this.subway = makeSubway(lines, stations, allSections);
    }

    private Map<Line, List<Station>> makeSubway(List<Line> lines, List<Station> stations, List<Section> allSections) {
        Map<Long, List<Section>> lineIdToUnorderedSections = new HashMap<>();
        for (Line line : lines) {
            lineIdToUnorderedSections.put(line.getId(), new ArrayList<>());
        }
        for (Section section : allSections) {
            lineIdToUnorderedSections.get(section.getLineId()).add(section);
        }
        Map<Line, List<Station>> subway = new HashMap<>();
        lineIdToUnorderedSections.forEach((key, value) -> {
            Sections sections = new Sections(value);
            List<Long> orderedStationIds = sections.findOrderedStationIds();
            List<Station> orderedStations = orderedStationIds.stream()
                .map(id -> stations.stream()
                    .filter(station -> Objects.equals(station.getId(), id))
                    .findFirst()
                    .orElseThrow(() -> new DomainException(ExceptionType.NO_EXISTENT_STATION)))
                .collect(Collectors.toList());
            subway.put(lines.stream()
                .filter(line -> line.getId() == key)
                .findFirst()
                .orElseThrow(() -> new DomainException(ExceptionType.NO_EXISTENT_LINE)), orderedStations);
        });
        return subway;
    }

    public Map<Line, List<Station>> getSubway() {
        return subway;
    }
}
