package subway.domain.section;

import java.util.List;
import subway.domain.Section;

public class SectionFactory {

    public static Section createRemoveCentralCase(final Section beforeSection, final Section nextSection) {
        return new Section(
                beforeSection.getPrevStation(),
                nextSection.getNextStation(),
                beforeSection.getDistance().plusValue(nextSection.getDistance())
        );
    }

    static Sections from(final List<Section> sections) {
        if (sections.isEmpty()) {
            return new EmptySections();
        }
        return new FilledSections(sections);
    }
}
