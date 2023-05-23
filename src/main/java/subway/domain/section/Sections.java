package subway.domain.section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections() {
        this(new ArrayList<>());
    }

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public static Sections merge(final Sections upSections, final Section section, final Sections downSections) {
        Collections.reverse(upSections.sections);

        final List<Section> sections = new ArrayList<>(upSections.sections);
        sections.add(section);
        sections.addAll(downSections.sections);

        return new Sections(sections);
    }

    public void add(final Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }
}
