package subway.controller.line.dto;

public class LineInsertWebRequest {
    private String name;
    private String color;

    public LineInsertWebRequest() {
    }

    public LineInsertWebRequest(String name, String color) {
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
