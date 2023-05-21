package subway.ui.dto.line;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import subway.domain.Line;
import subway.domain.Section;
import subway.ui.dto.section.SectionResponse;

public class LineResponse {

    private long id;
    private String name;
    private String color;
    @JsonProperty("sections")
    private List<SectionResponse> sectionResponses;

    public LineResponse(long id, String name, String color, List<SectionResponse> sectionResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sectionResponses = sectionResponses;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(),
                mapToSectionResponses(line.getSortedSections()));
    }

    private static List<SectionResponse> mapToSectionResponses(List<Section> sections) {
        return sections.stream()
                .map(SectionResponse::from)
                .collect(toList());
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

    public List<SectionResponse> getSectionResponses() {
        return sectionResponses;
    }
}
