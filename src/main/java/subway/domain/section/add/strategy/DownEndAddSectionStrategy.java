package subway.domain.section.add.strategy;

import subway.domain.Station;
import subway.domain.section.Section;
import subway.domain.section.Sections;

import static subway.domain.Position.DOWN;
import static subway.domain.Position.MID;

public class DownEndAddSectionStrategy implements AddSectionStrategy {

    @Override
    public void addSection(final Sections sections, final Section insertSection) {
        final Section originSection = sections.findDownSection();
        originSection.getDownStation().changeDirection(MID);
        changeDirection(insertSection);
        sections.addSection(insertSection);
    }

    public void changeDirection(final Section section) {
        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();

        upStation.changeDirection(MID);
        downStation.changeDirection(DOWN);
    }
}
