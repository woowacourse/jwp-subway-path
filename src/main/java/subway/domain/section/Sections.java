package subway.domain.section;

import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections (final List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public int size() {
        return sections.size();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }
}
