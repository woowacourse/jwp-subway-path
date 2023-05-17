package subway.domain;

import java.util.List;

public class Line {

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
            throw new IllegalArgumentException("호선 이름은 공백을 입력할 수 없습니다.");
        }
        if (name.length() < 1 || name.length() > 5) {
            throw new IllegalArgumentException("호선 이름은 1자 이상 5자 이하만 가능합니다.");
        }
    }

    private void validateColor(final String color) {
        if (color.isBlank()) {
            throw new IllegalArgumentException("호선 색깔은 공백을 입력할 수 없습니다.");
        }
        if (color.length() < 5 || color.length() > 20) {
            throw new IllegalArgumentException("호선 색깔은 5자 이상 20자 이하만 가능합니다.");
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
}
