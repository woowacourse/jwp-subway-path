package subway.domain.line;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.exception.NotInitializedLineException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Line {

    private Long id;
    private LineName name;
    private Color color;
    private Sections sections;
    private Station upBoundStation;
    private Station downBoundStation;

    public Line(final String name, final String color) {
        this(null, name, color, new Sections(List.of()), null, null);
    }

    public Line(final Long id, final String name, final String color, final Sections sections, Station upBoundStation, Station downBoundStation) {
        this.id = id;
        this.name = new LineName(name);
        this.color = new Color(color);
        this.sections = sections;
        this.upBoundStation = upBoundStation;
        this.downBoundStation = downBoundStation;
    }

    public void initStations(Station leftStation, Station rightStation, int distance) {
        if (sections.size() != 0) {
            throw new IllegalArgumentException("초기 등록할 때는 노선에 역이 하나도 없어야 합니다.");
        }

        sections.addSection(new Section(leftStation, rightStation, distance));
        upBoundStation = leftStation;
        downBoundStation = rightStation;
    }

    public void addStation(Station newStation, Station baseStation, Direction direction, int distance) {
        if (sections.isEmpty()) {
            throw new NotInitializedLineException();
        }
        if (upBoundStation.equals(baseStation) && Direction.LEFT.equals(direction)) {
            sections.addSection(new Section(newStation, baseStation, distance));
            upBoundStation = newStation;
            return;
        }
        if (downBoundStation.equals(baseStation) && Direction.RIGHT.equals(direction)) {
            sections.addSection(new Section(baseStation, newStation, distance));
            downBoundStation = newStation;
            return;
        }
        sections.split(newStation, baseStation, direction, distance);
    }

    public void deleteStation(Station deleteStation) {
        if (sections.isEmpty()) {
            throw new NotInitializedLineException();
        }
        if (sections.size() == 1) {
            deleteLastTwoStations(deleteStation);
            return;
        }
        if (deleteStation.equals(upBoundStation)) {
            deleteUpBoundStation();
            return;
        }
        if (deleteStation.equals(downBoundStation)) {
            deleteDownBoundStation();
            return;
        }
        deleteInnerStation(deleteStation);
    }

    private void deleteInnerStation(Station deleteStation) {
        Section leftSection = sections.getSectionByRightStation(deleteStation);
        Section rightSection = sections.getSectionByLeftStation(deleteStation);
        sections.mixSection(leftSection, rightSection);
    }

    private void deleteDownBoundStation() {
        Section section = sections.getSectionByRightStation(downBoundStation);
        sections.delete(section);
        downBoundStation = section.getLeftStation();
    }

    private void deleteUpBoundStation() {
        Section section = sections.getSectionByLeftStation(upBoundStation);
        sections.delete(section);
        upBoundStation = section.getRightStation();
    }

    private void deleteLastTwoStations(Station deleteStation) {
        Section section = sections.getSectionByLeftStation(deleteStation);
        if (section.getLeftStation().equals(deleteStation) || section.getRightStation().equals(deleteStation)) {
            sections = new Sections(List.of());
            upBoundStation = null;
            downBoundStation = null;
            return;
        }
        throw new IllegalArgumentException("삭제할 역이 존재하지 않습니다.");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public Stations getStations() {
        if (sections.isEmpty()) {
            return new Stations(List.of());
        }

        Map<Long, Section> indexedSections = new HashMap<>(this.sections.getSections());

        List<Station> stations = new ArrayList<>();
        Long nowStationId = upBoundStation.getId();
        stations.add(indexedSections.get(upBoundStation.getId()).getLeftStation());

        for (int i = 0; i < sections.size(); i++) {
            stations.add(indexedSections.get(nowStationId).getRightStation());
            Long beforeStationId = nowStationId;
            nowStationId = indexedSections.get(nowStationId).getRightStation().getId();
            indexedSections.remove(beforeStationId);
        }

        return new Stations(stations);
    }

    public Station getUpBoundStation() {
        return upBoundStation;
    }

    public Station getDownBoundStation() {
        return downBoundStation;
    }

    public Sections getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
