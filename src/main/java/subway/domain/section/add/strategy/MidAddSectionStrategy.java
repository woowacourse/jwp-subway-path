package subway.domain.section.add.strategy;

import subway.domain.section.Section;
import subway.domain.section.Sections;

public class MidAddSectionStrategy implements AddSectionStrategy {

    @Override
    public void addSection(final Sections sections, final Section insertSection) {
        final Section originSection = sections.findOriginSection(insertSection);
        final int originIndex = sections.findIndex(originSection);
        sections.delete(originSection);

        final Section upSection = insertSection.createUpSection(originSection);
        sections.addSection(originIndex, upSection);

        final Section downSection = insertSection.createDownSection(originSection);
        sections.addSection(originIndex + 1, downSection);
    }
}
