package subway.application.dto.line;

public class LineUpdateDto {
    private long id;
    private String name;
    private String color;

    public LineUpdateDto(long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
