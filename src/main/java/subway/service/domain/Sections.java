package subway.service.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> findContainsStation(Station station) {
        return sections.stream()
                .filter(section -> section.isContainsStation(station))
                .collect(Collectors.toList());
    }

    public boolean isContainsStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isContainsStation(station));
    }

    public Optional<Section> findPreviousStationStation(Station station) {
        return sections.stream()
                .filter(section -> section.isPreviousStationStation(station))
                .findFirst();
    }

    public Optional<Section> findNextStationStation(Station station) {
        return sections.stream()
                .filter(section -> section.isNextStationStation(station))
                .findFirst();
    }

    public RouteMapInLine createMap(LineProperty lineProperty) {
        Map<Station, List<Path>> map = new HashMap<>();

        for (Section section : sections) {
            putIfNotContains(map, section);
            map.get(section.getPreviousStation()).add(createPath(Direction.UP, lineProperty, section));
            map.get(section.getNextStation()).add(createPath(Direction.DOWN, lineProperty, section));
        }

        return new RouteMapInLine(map);
    }

    private Path createPath(Direction direction, LineProperty lineProperty, Section section) {
        if (Direction.UP == direction) {
            return new Path(
                    direction,
                    lineProperty,
                    section.getNextStation(),
                    Distance.from(section.getDistance())
            );
        }

        return new Path(
                direction,
                lineProperty,
                section.getPreviousStation(),
                Distance.from(section.getDistance())
        );
    }

    private void putIfNotContains(Map<Station, List<Path>> lineMap, Section section) {
        if (!lineMap.containsKey(section.getPreviousStation())) {
            lineMap.put(section.getPreviousStation(), new ArrayList<>());
        }

        if (!lineMap.containsKey(section.getNextStation())) {
            lineMap.put(section.getNextStation(), new ArrayList<>());
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }

}
