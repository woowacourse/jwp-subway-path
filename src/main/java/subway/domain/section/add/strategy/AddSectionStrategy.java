package subway.domain.section.add.strategy;

import subway.domain.section.Section;
import subway.domain.section.Sections;

@FunctionalInterface
public interface AddSectionStrategy {

    void addSection(final Sections sections, final Section insertSection);
}
