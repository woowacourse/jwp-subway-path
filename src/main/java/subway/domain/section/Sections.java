package subway.domain.section;

import java.util.List;
import subway.domain.Distance;
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

    public abstract Distance getTotalDistance();

    public List<Section> getSections() {
        return sections;
    }

    protected static Sections from(final List<Section> sections) {
        if (sections.isEmpty()) {
            return new EmptySections();
        }
        return new FilledSections(sections);
    }
}
