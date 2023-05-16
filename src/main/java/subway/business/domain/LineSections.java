package subway.business.domain;

import java.util.List;

public class LineSections {

    private final List<Section> sections;

    private LineSections(final List<Section> sections) {
        this.sections = sections;
    }

    public static LineSections from(final List<Section> sections) {
        return new LineSections(LineSectionsSortFactory.sort(List.copyOf(sections)));
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }

}
