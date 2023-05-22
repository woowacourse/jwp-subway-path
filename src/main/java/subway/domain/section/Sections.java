package subway.domain.section;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public static Sections initSections(final List<Section> sections) {
        Sections result = new EmptySections();
        if (sections.isEmpty()) {
            return result;
        }
        final Map<Station, Section> prevStationMap = sections.stream()
                .collect(toMap(Section::getPrevStation, Function.identity()));
        Section section = findHeadSection(sections);
        for (int i = 0; i < sections.size(); i++) {
            result = result.addSection(section);
            section = prevStationMap.get(section.getNextStation());
        }
        return from(sections);
    }

    private static Section findHeadSection(final List<Section> sections) {
        if (sections.size() == 1) {
            return sections.get(0);
        }
        final List<Station> nextStations = sections.stream()
                .map(Section::getNextStation)
                .collect(Collectors.toUnmodifiableList());
        return sections.stream()
                .filter(section -> !nextStations.contains(section.getPrevStation()))
                .findAny().orElseThrow(() -> new IllegalStateException("상행종점을 찾을 수 없습니다."));
    }
}
