package subway.service.dto;

public class LineDto {

    private final String name;
    private final String color;

    public LineDto(String name, String color) {
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
