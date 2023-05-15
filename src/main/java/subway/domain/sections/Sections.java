package subway.domain.sections;

import java.util.LinkedList;
import java.util.List;
import subway.domain.Section;
import subway.domain.Station;

public abstract class Sections {

    final List<Section> sections;

    Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public abstract List<Station> getAllStations();

    public abstract Sections addSection(final Section section);

    public abstract Sections removeStation(final Station station);

    public Sections getDifferenceOfSet(final Sections otherFilledSections) {
        final List<Section> result = new LinkedList<>(this.sections);
        result.removeAll(otherFilledSections.sections);
        return FilledSections.from(result);
    }

    public List<Section> getSections() {
        return sections;
    }
}
