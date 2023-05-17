package subway.service.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> findContainsThisStation(Station station) {
        return sections.stream()
                .filter(section -> section.isContainsStation(station))
                .collect(Collectors.toList());
    }

    public boolean isContainsThisStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isContainsStation(station));
    }

    public Optional<Section> findPreviousStationThisStation(Station station) {
        return sections.stream()
                .filter(section -> section.isPreviousStationThisStation(station))
                .findFirst();
    }

    public Optional<Section> findNextStationThisStation(Station station) {
        return sections.stream()
                .filter(section -> section.isNextStationThisStation(station))
                .findFirst();
    }

    public List<Section> getSections() {
        return sections;
    }

}
