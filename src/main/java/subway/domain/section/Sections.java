package subway.domain.section;

import java.util.List;
import subway.domain.Section;
import subway.domain.Station;

public abstract class Sections {

    protected final List<Section> sections;

    protected Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public abstract List<Station> getAllStations();

    public abstract Sections addSection(final Section section);

    public abstract Sections removeStation(final Station station);

    public abstract Sections getDifferenceOfSet(final Sections otherSections);

    public List<Section> getSections() {
        return sections;
    }
}
