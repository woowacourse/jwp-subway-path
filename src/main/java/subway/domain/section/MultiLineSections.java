package subway.domain.section;

import subway.domain.Station;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MultiLineSections {

    private final List<Section> sections;

    private MultiLineSections(List<Section> sections) {
        this.sections = sections;
    }

    public static MultiLineSections from(List<Section> sections) {
        return new MultiLineSections(sections);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getAllStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(toList());
    }
}
