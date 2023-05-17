package subway.domain.section.add.strategy;

import subway.domain.section.Section;
import subway.domain.section.Sections;

public class MidAddSectionStrategy implements AddSectionStrategy {

    @Override
    public void addSection(final Sections sections, final Section insertSection) {
        final Section originSection = sections.findOriginSection(insertSection);
        final Section upSection = insertSection.createUpSection(originSection);
        final Section downSection = insertSection.createDownSection(originSection);
        final int originIndex = sections.findIndex(originSection);
        sections.delete(originSection);
        sections.addSection(originIndex, upSection);
        sections.addSection(originIndex + 1, downSection);
    }
}
