package subway.domain;

import subway.Entity.LineEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Line {
    private final Long id;
    private final String name;
    private final String color;
    private final List<Section> sections;
    private final int extraFare;

    private Line(final Long id, final String name, final String color, final List<Section> sections, final int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.extraFare = extraFare;
    }

    public static Line of(final String name, final String color, final int extraFare) {
        Section initialSection = Section.createEmpty();
        List<Section> emptySections = new LinkedList<>();
        emptySections.add(initialSection);
        return new Line(null, name, color, emptySections, extraFare);
    }

    public static Line of(final long id, final String name, final String color, final List<Section> sections, final int extraFare) {
        return new Line(id, name, color, sections, extraFare);
    }

    public static LineEntity toEntity(final Line line) {
        return LineEntity.of(line.id, line.name, line.color, line.extraFare);
    }

    public void addSection(final Station upwardStation, final Station downwardStation, final int distance) {
        if (!hasAnyStation()) {
            Section emptySection = sections.get(0);
            addSection(emptySection, upwardStation, null, null);
            Section originalSection = findSectionContainsStationAsUpward(upwardStation)
                    .orElseThrow(() -> new IllegalStateException("[ERROR] 노선에 방금 구간을 추가했는데 찾을 수 없는 이상한 상황입니다."));
            addSection(originalSection, downwardStation, distance, null);
            return;
        }

        validateTwoStationsAlreadyExist(upwardStation, downwardStation);
        validateTwoStationsNotExist(upwardStation, downwardStation);

        Optional<Section> optionalUpwardSection = findSectionContainsStationAsUpward(upwardStation);
        Optional<Section> optionalDownwardSection = findSectionContainsStationAsDownward(downwardStation);

        if (optionalUpwardSection.isEmpty() && optionalDownwardSection.isPresent()) {
            Section section = optionalDownwardSection.get();
            Integer originalDistance = section.getDistance();
            if (originalDistance == null) {
                addSection(section, upwardStation, null, distance);
                return;
            }
            addSection(section, upwardStation, originalDistance - distance, distance);
            return;
        }

        if (optionalUpwardSection.isPresent() && optionalDownwardSection.isEmpty()) {
            Section section = optionalUpwardSection.get();
            Integer originalDistance = section.getDistance();
            if (originalDistance == null) {
                addSection(section, downwardStation, distance, null);
                return;
            }
            addSection(section, downwardStation, distance, originalDistance - distance);
            return;
        }

        throw new IllegalStateException();
    }

    private void validateTwoStationsNotExist(final Station upwardStation, final Station downwardStation) {
        if (!hasStation(upwardStation) && !hasStation(downwardStation)) {
            throw new IllegalArgumentException("[ERROR] 노선에 역을 1개씩 삽입해 주세요.");
        }
    }

    private void validateTwoStationsAlreadyExist(final Station upwardStation, final Station downwardStation) {
        if (hasStation(upwardStation) && hasStation(downwardStation)) {
            throw new IllegalArgumentException("[ERROR] 노선에 두 개의 역 모두가 이미 존재합니다.");
        }
    }

    private Optional<Section> findSectionContainsStationAsUpward(final Station upwardStation) {
        return sections.stream()
                .filter(section -> section.isUpwardStation(upwardStation))
                .findFirst();
    }

    private Optional<Section> findSectionContainsStationAsDownward(final Station downwardStation) {
        return sections.stream()
                .filter(section -> section.isDownwardStation(downwardStation))
                .findFirst();
    }

    public void addSection(final Section originalSection, final Station middleStation, final Integer upwardDistance, final Integer downwardDistance) {
        List<Section> splitedSections = originalSection.splitByStation(middleStation, upwardDistance, downwardDistance);
        sections.remove(originalSection);
        sections.addAll(splitedSections);
    }

    public void removeStation(final Station station) {
        if (!hasStation(station)) {
            throw new IllegalArgumentException("[ERROR] 노선에 해당 역이 존재하지 않습니다.");
        }
        Section downwardSection = findSectionContainsStationAsUpward(station)
                .orElseThrow(() -> new IllegalStateException("[ERROR] 노선에 해당 역은 존재하지만 구간이 존재하지 않는 이상한 상황입니다."));
        Section upwardSection = findSectionContainsStationAsDownward(station)
                .orElseThrow(() -> new IllegalStateException("[ERROR] 노선에 해당 역은 존재하지만 구간이 존재하지 않는 이상한 상황입니다."));

        Integer newDistance = null;
        if (!upwardSection.isEmptySection() && !downwardSection.isEmptySection()) {
            newDistance = upwardSection.getDistance() + downwardSection.getDistance();
        }
        Section combinedSection = Section.of(upwardSection.getUpwardStation(), downwardSection.getDownwardStation(), newDistance);
        sections.removeAll(List.of(upwardSection, downwardSection));
        sections.add(combinedSection);

        if (getStations().size() == 1) {
            removeStation(getStations().get(0));
        }
    }

    public boolean hasStation(final Station other) {
        return getStations().stream()
                .anyMatch(station -> station.equals(other));
    }

    public boolean hasAnyStation() {
        return getStations().size() != 0;
    }

    public boolean isSameName(final Line line) {
        return this.name.equals(line.name);
    }

    public boolean isSameColor(final Line line) {
        return this.color.equals(line.color);
    }

    public List<Station> getStationsUpwardToDownward() {
        List<Station> stations = new ArrayList<>();

        Section upwardEnd = getUpwardEndSection();
        Section downwardEnd = getDownwardEndSection();

        Section section = upwardEnd;
        addStationIfNotNull(stations, upwardEnd.getUpwardStation());
        Station station = upwardEnd.getDownwardStation();
        while (!section.equals(downwardEnd)) {
            addStationIfNotNull(stations, station);
            section = findNextSectionToDownward(section);
            station = section.getDownwardStation();
        }
        return stations;
    }

    private void addStationIfNotNull(final List<Station> stations, final Station station) {
        if (!station.isEmpty()) {
            stations.add(station);
        }
    }

    private Section getUpwardEndSection() {
        return sections.stream()
                .filter(Section::isEndOfUpward)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("[ERROR] 상행 종점을 찾을 수 없습니다."));
    }

    private Section getDownwardEndSection() {
        return sections.stream()
                .filter(Section::isEndOfDownward)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("[ERROR] 하행 종점을 찾을 수 없습니다."));
    }

    private Section findNextSectionToDownward(final Section section) {
        return sections.stream()
                .filter(next -> next.isUpwardStation(section.getDownwardStation()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("[ERROR] 다음 구간을 찾을 수 없습니다."));
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        for (Section section : sections) {
            stations.add(section.getUpwardStation());
            stations.add(section.getDownwardStation());
        }
        return stations.stream()
                .distinct()
                .filter(station -> !station.equals(Station.createEmpty()))
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Section> getSectionsExceptEmpty() {
        List<Section> sections = new ArrayList<>(this.sections);
        sections.removeAll(List.of(getDownwardEndSection(), getUpwardEndSection()));
        return sections;
    }

    public int getExtraFare() {
        return extraFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    public boolean isSameId(final long id) {
        return this.id == id;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                '}';
    }
}
