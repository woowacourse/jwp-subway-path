package subway.dto;

import subway.domain.Section;

import java.util.ArrayList;
import java.util.List;

public class AddSections {
    private final List<Section> sections;

    public AddSections() {
        sections = new ArrayList<>();
    }

    public void insert(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }
}
