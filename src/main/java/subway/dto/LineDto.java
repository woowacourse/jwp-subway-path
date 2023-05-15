package subway.dto;

import subway.domain.line.Line;

public class LineDto {

    private final Long id;
    private final String name;

    private LineDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static LineDto from(Line line) {
        return new LineDto(line.getId(), line.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
