package subway.controller.dto.response;

import subway.service.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<SectionResponse> sections;

    private LineResponse(Long id,
                         String name,
                         String color,
                         List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(
                line.getLineProperty().getId(),
                line.getLineProperty().getName(),
                line.getLineProperty().getColor(),
                line.getSections().stream()
                        .map(SectionResponse::from)
                        .collect(Collectors.toList())
        );
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

    public List<SectionResponse> getSections() {
        return sections;
    }

}
