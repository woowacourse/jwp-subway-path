package subway.domain.line;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Line {
    private Long id;
    private Name name;
    private Color color;
    private Stations stations;
    private Sections sections;

    public Line() {
    }

    public Line(final String name, final String color) {
        this(null, name, color, new Stations(List.of()), new Sections(List.of()));
    }

    public Line(final Long id, final String name, final String color, final Stations stations, final Sections sections) {
        validate(stations, sections);
        this.id = id;
        this.name = new Name(name);
        this.color = new Color(color);
        this.stations = stations;
        this.sections = sections;
    }

    public Line(final Long id, final String name, final String color, final Stations stations, final Sections sections, final Long upBoundStationId) {
        this(id, name, color, sortStation(stations, sections, upBoundStationId), sections);
    }

    private static Stations sortStation(final Stations randomStations, final Sections sections, final Long upBoundStationId) {
        Map<Long, Station> indexedStations = new HashMap<>();
        randomStations.getStations().forEach(station -> indexedStations.put(station.getId(), station));

        Map<Long, Section> indexedSections = new HashMap<>();
        sections.getSections().forEach(section -> indexedSections.put(section.getLeftStation().getId(), section));

        List<Station> stations = new ArrayList<>();
        Long nowStationId = upBoundStationId;
        while (indexedSections.size() != 0) {
            stations.add(indexedStations.get(nowStationId));
            Long beforeStationId = nowStationId;
            nowStationId = indexedSections.get(nowStationId).getRightStation().getId();
            indexedSections.remove(beforeStationId);
        }
        stations.add(indexedStations.get(nowStationId));

        return new Stations(stations);
    }

    private void validate(final Stations stations, final Sections sections) {
        if ((stations.isEmpty() && sections.isEmpty()) || stations.isCorrectSectionsSize(sections)) {
            return;
        }
        throw new IllegalArgumentException("역의 수에 따른 간선의 수가 올바르지 않습니다.");
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
        return stations;
    }

    public Station getUpBoundStation() {
        return stations.getFirstStation();
    }

    public Station getDownBoundStation() {
        return stations.getLastStation();
    }

    public Sections getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
