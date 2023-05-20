package subway.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import subway.controller.exception.InvalidStationException;

public class Line {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 10;

    private final String name;
    private final String color;
    private final Sections sections;

    public Line(final String name, final String color) {
        this(name, color, Collections.emptyList());
    }

    public Line(final String name, final String color, final List<Section> sections) {
        validate(name);
        this.name = name;
        this.color = color;
        this.sections = new Sections(sections);
    }

    private void validate(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new InvalidStationException(String.format("노선 이름은 %d~%d자 사이여야 합니다.", MIN_NAME_LENGTH, MAX_NAME_LENGTH));
        }
    }

    public void registerSection(final Station source, final Station target, final int distance) {
        sections.register(source, target, distance);
    }

    public void deleteStation(final Station station) {
        sections.delete(station);
    }

    public boolean hasName(final String name) {
        return this.name.equals(name);
    }

    public List<Station> stations() {
        return sections.getOrderedStations();
    }

    public List<Section> sections() {
        return sections.get();
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Line)) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
