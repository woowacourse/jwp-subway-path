package subway.dto;

import subway.domain.Section;

import java.util.ArrayList;
import java.util.List;

public class UpdateSections {
    private final List<Section> sections;

    public UpdateSections() {
        sections = new ArrayList<>();
    }

    public void insert(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }
}
