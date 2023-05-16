package subway.domain.section;

import subway.domain.station.Station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sections {

    public static final int BOUND_STATION_INDEX = 0;

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public Station findUpBoundStation() {
        List<Station> upperStations = sections.stream()
                .map(Section::getLeftStation)
                .collect(Collectors.toList());
        List<Station> downStations = sections.stream()
                .map(Section::getRightStation)
                .collect(Collectors.toList());

        upperStations.removeAll(downStations);

        return upperStations.get(BOUND_STATION_INDEX);
    }

    public Station findDownBoundStation() {
        List<Station> downStations = sections.stream()
                .map(Section::getRightStation)
                .collect(Collectors.toList());
        List<Station> upperStations = sections.stream()
                .map(Section::getLeftStation)
                .collect(Collectors.toList());

        downStations.removeAll(upperStations);

        return downStations.get(BOUND_STATION_INDEX);
    }

    public boolean isContainStation(Station station) {
        List<String> stations = findAllStation().stream()
            .map(Station::getName)
            .collect(Collectors.toList());
        return stations.contains(station.getName());
    }

    private List<Station> findAllStation() {
        List<Station> stations = sections.stream()
            .map(Section::getLeftStation)
            .collect(Collectors.toList());
        stations.add(findDownBoundStation());
        return stations;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Section findSection(Station baseStation, String direction) {
        if (direction.equals("left")) {
            return sections.stream()
                    .filter(section -> section.getRightStation().equals(baseStation))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
        }
        return sections.stream()
                .filter(section -> section.getLeftStation().equals(baseStation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public List<Section> getSections() {
        return sections;
    }
}
