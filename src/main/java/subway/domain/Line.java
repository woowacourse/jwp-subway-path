package subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import subway.domain.charge.Charge;

public class Line {

    private Long id;
    private String name;
    private Charge extraCharge;

    private List<Section> sections;

    public Line(String name, Charge extraCharge) {
        this.name = name;
        this.extraCharge = extraCharge;
    }

    public Line(Long id, String name, Charge extraCharge, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.extraCharge = extraCharge;
        this.sections = sections;
    }

    public Line(Long id, String name, Charge extraCharge) {
        this.id = id;
        this.name = name;
        this.extraCharge = extraCharge;
    }

    public static Line createLine(String name, Charge extraCharge, Station upStation, Station downStation, int distance) {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(upStation, downStation, distance));
        return new Line(null, name, extraCharge, sections);
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        validateStations(upStation, downStation);
        Direction direction = findDirection(upStation);
        direction.add(sections, upStation, downStation, distance);
    }

    private void validateStations(Station upStation, Station downStation) {
        if (isAlreadyExistBoth(upStation, downStation)) {
            throw new IllegalArgumentException("해당 노선에 두 역이 모두 존재합니다.");
        }
        if (isNothingExist(upStation, downStation)) {
            throw new IllegalArgumentException("해당 노선에 두 역이 모두 존재하지 않습니다.");
        }
    }

    private Direction findDirection(Station upStation) {
        if (getStations().stream().anyMatch(station -> station.equals(upStation))) {
            return Direction.DOWN;
        }
        return Direction.UP;
    }

    private boolean isAlreadyExistBoth(Station upStation, Station downStation) {
        return new HashSet<>(getStations()).containsAll(List.of(upStation, downStation));
    }

    private boolean isNothingExist(Station upStation, Station downStation) {
        return !getStations().contains(upStation) && !getStations().contains(downStation);
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
                    section1.getDistance() + section2.getDistance());
            int removedIndex = sections.indexOf(section1);
            sections.remove(section1);
            sections.remove(section2);
            sections.add(removedIndex, newSection);
        }
    }

    private static void validateTargetSections(List<Section> targetSections) {
        if (targetSections.isEmpty()) {
            throw new IllegalArgumentException("해당 역이 해당 노선에 존재하지 않습니다.");
        }
        if (targetSections.size() > 2) {
            throw new IllegalArgumentException("해당 노선에 갈래길이 존재합니다. 확인해주세요.");
        }
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Charge getExtraCharge() {
        return extraCharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", extraCharge=" + extraCharge +
                '}';
    }
}
