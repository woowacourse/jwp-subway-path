package subway.domain;

import java.util.List;

public class LineSections {

    private final Line line;
    private final List<LineSection> sections;

    private LineSections(final Line line, final List<LineSection> sections) {
        this.line = line;
        this.sections = sections;
    }

    public static LineSections createWithSort(final Line line, final List<LineSection> unsortedSections) {
        final List<LineSection> sortedSections = LineSectionsSortFactory.sort(List.copyOf(unsortedSections));
        return new LineSections(line, sortedSections);
    }

    public Line getLine() {
        return line;
    }

    public List<LineSection> getSections() {
        return List.copyOf(sections);
    }

}
