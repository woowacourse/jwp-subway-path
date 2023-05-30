package subway.domain.section.delete.strategy;

import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.domain.section.Sections;

public class MidDeleteStationStrategy implements DeleteStationStrategy {

    @Override
    public void deleteSection(final Sections sections, final Station removeStation) {
        final Section upSection = sections.getSectionBy(removeStation, Section::getDownStation);
        final Section downSection = sections.getSectionBy(removeStation, Section::getUpStation);
        sections.delete(upSection);
        sections.delete(downSection);

        final Station upStation = upSection.getUpStation();
        final Station downStation = downSection.getDownStation();
        final Distance distance = upSection.getDistance().add(downSection.getDistance());
        sections.addSection(Section.of(upStation, downStation, distance));
    }
}
