package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import subway.domain.exception.EmptyPathException;

public class Path {

    private final List<Section> sections;

    public Path(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> section.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public Distance getDistance() {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(Distance::sum)
                .orElseThrow(EmptyPathException::new);
    }
}
