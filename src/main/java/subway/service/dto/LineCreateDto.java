package subway.service.dto;

public class LineCreateDto {

    private final String name;
    private final String color;

    public LineCreateDto(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
