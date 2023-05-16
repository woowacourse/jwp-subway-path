package subway.domain.section;

import java.util.Collections;
import java.util.List;
import subway.domain.Station;

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
        throw new IllegalArgumentException("비어있는 Line에서 역을 지울순 없습니다.");
    }

    @Override
    public Sections getDifferenceOfSet(final Sections otherSections) {
        return this;
    }
}
