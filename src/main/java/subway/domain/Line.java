package subway.domain;

import java.util.*;

public class Line {

    private final Long id;
    private final LineName name;
    private final LineColor color;
    private final Station firstStation;
    private final Station lastStation;
    private final List<Section> sections;

    // TODO: 5/13/23 null 해결하기
    public Line(final LineName name, final LineColor color) {
        this(null, name, color, null, null, new ArrayList<>());
    }
    public Line(final LineName name, final LineColor color, final Station firstStation, final Station lastStation, final List<Section> sections) {
        this(null, name, color, firstStation, lastStation, sections);
    }

    public Line(final Long id, final LineName name, final LineColor color, final Station firstStation, final Station lastStation, final List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.firstStation = firstStation;
        this.lastStation = lastStation;
        this.sections = sections;
    }

    public Line addMiddleSection(final Section upSection, final Section downSection) {
        Map<Station, Section> upToDown = getUpStationToDownStation();
        if (!upToDown.containsKey(upSection.getUpStation())) {
            throw new IllegalArgumentException("해당 노선에 존재하지 않는 상행역입니다.");
        }
        Section targetSection = upToDown.get(upSection.getUpStation());
        if (targetSection.isPossibleDivideTo(upSection, downSection)) {
            List<Section> editSections = new ArrayList<>(sections);
            editSections.remove(targetSection);
            editSections.add(upSection);
            editSections.add(downSection);
            return new Line(id, name, color, firstStation, lastStation, editSections);
        }
        throw new IllegalArgumentException("역을 추가할 수 없습니다.");
    }

    public Line addFirstSection(final Section section) {
        if (!firstStation.equals(section.getDownStation())) {
            throw new IllegalArgumentException("기존 상행종점과 이을 수 없는 구간입니다.");
        }
        List<Section> editSections = new ArrayList<>(sections);
        editSections.add(section);
        return new Line(id, name, color, section.getUpStation(), lastStation, editSections);
    }

    public Line addLastSection(final Section section) {
        if (!lastStation.equals(section.getUpStation())) {
            throw new IllegalArgumentException("기존 하행종점과 이을 수 없는 구간입니다.");
        }
        List<Section> editSections = new ArrayList<>(sections);
        editSections.add(section);
        return new Line(id, name, color, firstStation, section.getDownStation(), editSections);
    }

    public Line addInitSection(final Section section) {
        if (!sections.isEmpty()) {
            throw new IllegalArgumentException("이미 구간이 있는 노선입니다.");
        }
        List<Section> editSections = new ArrayList<>(sections);
        editSections.add(section);
        return new Line(id, name, color, section.getUpStation(), section.getDownStation(), editSections);
    }

    // TODO: 5/14/23 상행역 하행역 필드로 둘지 고민해보기
    public Line removeStation(final Station station) {
        Optional<Section> maybeUpSection = sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findAny();
        Optional<Section> maybeDownSection = sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findAny();
        List<Section> editSections = new ArrayList<>(sections);
        if (maybeUpSection.isEmpty() && maybeDownSection.isEmpty()) {
            throw new IllegalArgumentException("삭제할 구간이 없습니다.");
        }
        if (maybeUpSection.isEmpty()) {
            Section downSection = maybeDownSection.get();
            editSections.remove(downSection);
            return new Line(id, name, color, downSection.getDownStation(), lastStation, editSections);
        }
        if (maybeDownSection.isEmpty()) {
            Section upSection = maybeUpSection.get();
            editSections.remove(upSection);
            return new Line(id, name, color, firstStation, upSection.getUpStation(), editSections);
        }
        Section upSection = maybeUpSection.get();
        Section downSection = maybeDownSection.get();
        editSections.remove(upSection);
        editSections.remove(downSection);
        Section mergedSection = upSection.mergeWith(downSection);
        editSections.add(mergedSection);
        return new Line(id, name, color, firstStation, lastStation, editSections);
    }

    public List<Station> findAllStation() {
        Map<Station, Section> upStationToDownStation = getUpStationToDownStation();
        List<Station> stations = new ArrayList<>();
        Station station = firstStation;
        stations.add(station);
        while (upStationToDownStation.containsKey(station)) {
            station = upStationToDownStation.get(station).getDownStation();
            stations.add(station);
        }
        return stations;
    }

    private Map<Station, Section> getUpStationToDownStation() {
        if (sections == null || sections.isEmpty()) {
            throw new IllegalArgumentException("해당 노선은 비어있습니다.");
        }
        Map<Station, Section> upStationToDownStation = new HashMap<>();
        for (Section section : sections) {
            upStationToDownStation.put(section.getUpStation(), section);
        }
        return upStationToDownStation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getColor() {
        return color.getValue();
    }

    public Station getFirstStation() {
        return firstStation;
    }

    public Station getLastStation() {
        return lastStation;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(firstStation, line.firstStation) && Objects.equals(lastStation, line.lastStation) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, firstStation, lastStation, sections);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", Name=" + name +
                ", color=" + color +
                ", firstStation=" + firstStation +
                ", lastStation=" + lastStation +
                ", sections=" + sections +
                '}';
    }
}
