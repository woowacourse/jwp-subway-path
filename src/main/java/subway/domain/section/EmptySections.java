package subway.domain.section;

import java.util.Collections;
import java.util.List;
import subway.domain.Distance;
import subway.domain.Station;
import subway.exception.EmptySectionsCanNotRemoveSection;
import subway.exception.EmptySectionsHasNotDistance;

public class EmptySections extends Sections {

    public EmptySections() {
        super(Collections.emptyList());
    }

    @Override
    public List<Station> getAllStations() {
        return Collections.emptyList();
    }

    @Override
    public Sections addSection(final Section section) {
        if (section == null) {
            return this;
        }
        return from(List.of(section));
    }

    @Override
    public Sections removeStation(final Station station) {
        throw new EmptySectionsCanNotRemoveSection();
    }

    @Override
    public Sections getDifferenceOfSet(final Sections otherSections) {
        return this;
    }

    @Override
    public Distance getTotalDistance() {
        throw new EmptySectionsHasNotDistance();
    }
}
