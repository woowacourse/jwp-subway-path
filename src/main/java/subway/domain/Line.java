package subway.domain;

import java.util.List;

public class Line {

    private static final String BLANK_NAME_ERROR_MESSAGE = "호선 이름은 공백을 입력할 수 없습니다.";
    private static final String NAME_LENGTH_BOUNDARY_MESSAGE = "호선 이름은 1자 이상 5자 이하만 가능합니다.";
    private static final String BLANK_COLOR_ERROR_MESSAGE = "호선 색깔은 공백을 입력할 수 없습니다.";
    private static final String COLOR_LENGTH_BOUNDARY_MESSAGE = "호선 색깔은 5자 이상 20자 이하만 가능합니다.";
    private static final int NAME_LOWER_BOUND = 1;
    private static final int NAME_UPPER_BOUND = 5;
    private static final int COLOR_LOWER_BOUND = 5;
    private static final int COLOR_UPPER_BOUND = 20;

    private Long id;
    private final String name;
    private final String color;
    private Sections sections;

    public Line(final Long id, final String name, final String color, final Sections sections) {
        validateName(name);
        validateColor(color);
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(final String name, final String color) {
        validateName(name);
        validateColor(color);
        this.name = name;
        this.color = color;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException(BLANK_NAME_ERROR_MESSAGE);
        }
        if (name.length() < NAME_LOWER_BOUND || name.length() > NAME_UPPER_BOUND) {
            throw new IllegalArgumentException(NAME_LENGTH_BOUNDARY_MESSAGE);
        }
    }

    private void validateColor(final String color) {
        if (color.isBlank()) {
            throw new IllegalArgumentException(BLANK_COLOR_ERROR_MESSAGE);
        }
        if (color.length() < COLOR_LOWER_BOUND || color.length() > COLOR_UPPER_BOUND) {
            throw new IllegalArgumentException(COLOR_LENGTH_BOUNDARY_MESSAGE);
        }
    }

    public List<Station> sortStations() {
        return sections.sortStations();
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
}
