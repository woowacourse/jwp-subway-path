package subway.line.application.dto;

public class LineSavingInfo {
    private final String name;
    private final String color;

    public LineSavingInfo(String name, String color) {
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
