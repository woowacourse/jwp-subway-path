package subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import subway.exception.ErrorCode;
import subway.exception.NotFoundException;

public class Sections {
    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public boolean isTerminalStation(final Station station) {
        Stations startStations = new Stations(
                sections.stream()
                        .map(Section::getStartStation)
                        .collect(Collectors.toList())
        );

        Stations endStations = new Stations(
                sections.stream()
                        .map(Section::getEndStation)
                        .collect(Collectors.toList())
        );

        return !(startStations.contains(station) && endStations.contains(station));
    }

    public Section getSectionByStartStation(final Station startStation) {
        return sections.stream()
                .filter(section -> section.isSameStartStation(startStation))
                .findAny()
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_SECTION));
    }

    public Section getSectionByEndStation(final Station endStation) {
        return sections.stream()
                .filter(section -> section.isSameEndStation(endStation))
                .findAny()
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_SECTION));
    }
}
