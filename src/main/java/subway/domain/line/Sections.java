package subway.domain.line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import subway.domain.vo.Distance;
import subway.exception.line.BothStationsAlreadyExistException;
import subway.exception.line.BothStationsDoNotExistException;
import subway.exception.line.ForkedRoadException;
import subway.exception.line.StationDoesNotExistOnLineException;

public class Sections {

    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections createSections(Section initialSection) {
        List<Section> sections = new ArrayList<>();
        sections.add(initialSection);
        return new Sections(sections);
    }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        validateStations(upStation, downStation);
        Direction direction = findDirection(upStation);
        direction.add(sections, upStation, downStation, distance);
    }

    private void validateStations(Station upStation, Station downStation) {
        if (isAlreadyExistBoth(upStation, downStation)) {
            throw new BothStationsAlreadyExistException();
        }
        if (isNothingExist(upStation, downStation)) {
            throw new BothStationsDoNotExistException();
        }
    }

    private boolean isAlreadyExistBoth(Station upStation, Station downStation) {
        return hasStation(upStation) && hasStation(downStation);
    }

    private boolean isNothingExist(Station upStation, Station downStation) {
        return !hasStation(upStation) && !hasStation(downStation);
    }

    private Direction findDirection(Station upStation) {
        if (hasStation(upStation)) {
            return Direction.DOWN;
        }
        return Direction.UP;
    }

    public void deleteStation(Station station) {
        List<Section> targetSections = sections.stream()
                .filter(section -> section.hasStation(station))
                .collect(Collectors.toList());
        validateTargetSections(targetSections);

        if (targetSections.size() == 1) {
            sections.remove(targetSections.get(0));
        }
        if (targetSections.size() == 2) {
            Section section1 = targetSections.get(0);
            Section section2 = targetSections.get(1);

            Section newSection = new Section(section1.getUpStation(), section2.getDownStation(),
                    section1.getDistance().add(section2.getDistance()));
            sections.remove(section1);
            sections.remove(section2);
            sections.add(newSection);
        }
    }

    private static void validateTargetSections(List<Section> targetSections) {
        if (targetSections.isEmpty()) {
            throw new StationDoesNotExistOnLineException();
        }
        if (targetSections.size() > 2) {
            throw new ForkedRoadException();
        }
    }

    public boolean hasStation(Station station) {
        return sections.stream()
                .anyMatch(it -> it.hasStation(station));
    }

    public List<Station> getStations() {
        Map<Station, Station> stationToStation = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        Set<Station> ups = new HashSet<>(stationToStation.keySet());
        ups.removeAll(stationToStation.values());

        List<Station> result = new ArrayList<>(ups);
        Station targetStation = result.get(0);
        while (stationToStation.containsKey(targetStation)) {
            Station next = stationToStation.get(targetStation);
            result.add(next);
            targetStation = next;
        }
        return result;
    }

    public List<Long> getStationIds() {
        return getStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
