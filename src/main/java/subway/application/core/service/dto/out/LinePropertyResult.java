package subway.application.core.service.dto.out;

import subway.application.core.domain.LineProperty;

public class LinePropertyResult {

    private final Long id;
    private final String name;
    private final String color;

    public LinePropertyResult(LineProperty lineProperty) {
        this.id = lineProperty.getId();
        this.name = lineProperty.getName();
        this.color = lineProperty.getColor();
    }

    public LinePropertyResult(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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
