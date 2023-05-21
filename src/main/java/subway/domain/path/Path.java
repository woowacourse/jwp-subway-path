package subway.domain.path;

import subway.domain.Section;

import java.util.List;

public class Path {

    private final List<Section> sections;

    public Path(final List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }
}
