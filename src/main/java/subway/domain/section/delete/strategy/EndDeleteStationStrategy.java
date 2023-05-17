package subway.domain.section.delete.strategy;

import subway.domain.Station;
import subway.domain.section.Section;
import subway.domain.section.Sections;

public class EndDeleteStationStrategy implements DeleteStationStrategy {

    @Override
    public void deleteSection(final Sections sections, final Station removeStation) {
        final Section endSection = sections.getEndSectionBy(removeStation);
        sections.delete(endSection);
        final Station otherStation = endSection.findOtherStation(removeStation);
        otherStation.changeDirection(removeStation.getPosition());
    }
}
