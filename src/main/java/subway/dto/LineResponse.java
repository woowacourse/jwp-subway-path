package subway.dto;

import subway.domain.Line;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final Integer additionalFare;

    private LineResponse(Long id, String name, String color, Integer additionalFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare());
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

    public Integer getAdditionalFare() {
        return additionalFare;
    }
}
