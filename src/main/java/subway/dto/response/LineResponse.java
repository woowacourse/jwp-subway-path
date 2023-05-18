package subway.dto.response;

import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<SectionResponse> sectionResponses;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sectionResponses = sections;
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

    public List<SectionResponse> getSectionResponses() {
        return sectionResponses;
    }
}
