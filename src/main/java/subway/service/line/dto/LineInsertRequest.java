package subway.service.line.dto;

import subway.controller.line.dto.LineInsertWebRequest;

public class LineInsertRequest {
    private String name;
    private String color;

    public LineInsertRequest() {
    }

    public LineInsertRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public LineInsertRequest(LineInsertWebRequest lineInsertWebRequest) {
        this(lineInsertWebRequest.getName(), lineInsertWebRequest.getColor());
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
