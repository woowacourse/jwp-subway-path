package subway.domain;

import subway.exception.ApiIllegalArgumentException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Line {

    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_COLOR_LENGTH = 20;

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;

    public Line(final String name, final String color) {
        this(null, name, color, Collections.emptyList());
    }

    public Line(final Long id, final String name, final String color, final List<Section> sections) {
        validateName(name);
        validateColor(color);
        this.id = id;
        this.name = name.strip();
        this.color = color.strip();
        this.sections = new Sections(new ArrayList<>(sections));
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new ApiIllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (name.strip().length() > MAX_NAME_LENGTH) {
            throw new ApiIllegalArgumentException("이름은 " + MAX_NAME_LENGTH + "자 이하여야합니다.");
        }
    }

    private void validateColor(final String color) {
        if (color == null || color.isBlank()) {
            throw new ApiIllegalArgumentException("색상은 비어있을 수 없습니다.");
        }
        if (color.strip().length() > MAX_COLOR_LENGTH) {
            throw new ApiIllegalArgumentException("색상은 " + MAX_COLOR_LENGTH + "자 이하여야합니다.");
        }
    }

    public void addSection(final Section section) {
        sections.add(section);
    }

    public void removeStation(final Station station) {
        sections.remove(station);
    }

    public List<Station> findOrderedStation() {
        return sections.findOrderedStation();
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
        return sections.getSections();
    }

    public Map<Station, Station> getSectionsByMap() {
        return sections.getStationUpToDown();
    }
}
