package subway.service.domain;

import java.util.List;
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

    public List<Section> getSections() {
        return sections;
    }

}
