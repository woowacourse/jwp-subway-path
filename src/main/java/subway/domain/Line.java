package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Line {

    private static final int MIN_LENGTH_NAME = 1;
    private static final int MAX_LENGTH_NAME = 10;

    private final Long id;
    private final String name;
    private final String color;
    private final int charge;
    private final Sections sections;

    public Line(Long id, String name, String color, int charge, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.charge = charge;
        this.sections = sections;
        validateNameLength(name);
    }

    public Line(String name, String color, int charge, Sections sections) {
        this(null, name, color, charge, sections);
    }

    private void validateNameLength(String name) {
        if(name.length()<MIN_LENGTH_NAME || name.length() > MAX_LENGTH_NAME) {
            throw new IllegalArgumentException("노선 이름은 1자 이상 10자 이하여야 합니다.");
        }
    }

    public void addInitialStations(Station upStation, Station downStation, Distance distance) {
        if(!isEmptyLine()) {
            throw new IllegalArgumentException("빈 노선이 아닙니다.");
        }
        sections.addInitialSection(upStation, downStation, distance);
    }

    public void addStation(Station upStation, Station downStation, Direction directionOfBase,Distance distance) {
        sections.addAdditionalSection(upStation, downStation, directionOfBase, distance);
    }

    public List<Station> findStations() {
        return sections.findAllStations();
    }

    public Station findStationById(Long stationId) {
        return findStations().stream()
                .filter(station -> station.getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 노선에 존재하지 않습니다"));
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }

    private boolean isEmptyLine() {
        return sections.isEmpty();
    }

    public boolean hasSameName(String otherName) {
        return this.name.equals(otherName);
    }

    public boolean hasSameColor(String otherColor) {
        return this.color.equals(otherColor);
    }

    public boolean hasSameSection(Section section) {
        return sections.hasSameSection(section);
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name)
                && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
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
    
    public int getCharge() {
        return charge;
    }
    
    public List<Section> getSections() {
        return new LinkedList<>(sections.getSections());
    }
}
