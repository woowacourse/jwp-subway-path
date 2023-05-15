package subway.domain.section;

import java.util.List;
import subway.domain.Section;

public class SectionFactory {

    public static Section createRemoveMiddleCase(final Section beforeSection, final Section nextSection) {
        return new Section(
                beforeSection.getPrevStation(),
                nextSection.getNextStation(),
                beforeSection.getDistance().plusValue(nextSection.getDistance())
        );
    }

    public static Section createAddConnectedPrevCase(final Section section, final Section originSection) {
        return new Section(
                section.getNextStation(),
                originSection.getNextStation(),
                originSection.getDistance().minusValue(section.getDistance())
        );
    }

    public static Section createAddConnectedNextCase(final Section section, final Section originSection) {
        return new Section(
                originSection.getPrevStation(),
                section.getPrevStation(),
                originSection.getDistance().minusValue(section.getDistance())
        );
    }

    static Sections from(final List<Section> sections) {
        if (sections.isEmpty()) {
            return new EmptySections();
        }
        return new FilledSections(sections);
    }
}
