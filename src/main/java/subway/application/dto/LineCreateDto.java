package subway.application.dto;

public class LineCreateDto {
    private String name;
    private String color;

    public LineCreateDto(String name, String color) {
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
