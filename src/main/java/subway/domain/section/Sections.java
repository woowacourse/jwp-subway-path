package subway.domain.section;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;

public class Sections {
    private final List<Section> sections;

    public Sections() {
        this.sections = new LinkedList<>();
    }

    public void addInitialStations(final Section section) {
        if (!sections.isEmpty()) {
            throw new IllegalStateException();
        }

        this.sections.add(section);
    }

    public void addFirstStation(
            final Station from,
            final Station station,
            final StationDistance stationDistance
    ) {
        final List<Section> collect = sections.stream()
                .filter(section -> section.matchFromStation(from))
                .collect(Collectors.toList());
        validateUniqueSection(collect);

        final Section firstSection = collect.get(0);
        sections.add(firstSection.attachFront(station, stationDistance));
    }

    private void validateUniqueSection(final List<Section> collect) {
        if (collect.size() != 1) {
            throw new IllegalStateException();
        }
    }

    public Section peekByFromStation(final Station from) {
        return sections.stream()
                .filter(section -> section.matchFromStation(from))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public List<Section> getSections() {
        return sections;
    }
}
