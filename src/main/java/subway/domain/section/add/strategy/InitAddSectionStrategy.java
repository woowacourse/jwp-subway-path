package subway.domain.section.add.strategy;

import subway.domain.Station;
import subway.domain.section.Section;
import subway.domain.section.Sections;

import static subway.domain.Position.DOWN;
import static subway.domain.Position.UP;

public class InitAddSectionStrategy implements AddSectionStrategy {

    @Override
    public void addSection(final Sections sections, final Section insertSection) {
        initPosition(insertSection);
        sections.addSection(insertSection);
    }

    private void initPosition(final Section section) {
        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();

        upStation.changeDirection(UP);
        downStation.changeDirection(DOWN);
    }
}
