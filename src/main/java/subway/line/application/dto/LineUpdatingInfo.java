package subway.line.application.dto;

public class LineUpdatingInfo {
    private final String name;
    private final String color;

    public LineUpdatingInfo(String name, String color) {
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
