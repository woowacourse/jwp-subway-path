package subway.domain.section.add.strategy;

import subway.domain.Station;
import subway.domain.section.Section;
import subway.domain.section.Sections;

import static subway.domain.Position.MID;
import static subway.domain.Position.UP;

public class UpEndAddSectionStrategy implements AddSectionStrategy {

    @Override
    public void addSection(final Sections sections, final Section insertSection) {
        final Section originSection = sections.findUpSection();
        originSection.getUpStation().changeDirection(MID);
        changeDirection(insertSection);
        sections.addSection(0, insertSection);
    }

    private void changeDirection(final Section section) {
        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();

        upStation.changeDirection(UP);
        downStation.changeDirection(MID);
    }
}
