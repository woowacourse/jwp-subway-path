package subway.domain.path;

import java.util.List;

import subway.domain.section.Section;

public class PathSections {


    private final List<Section> orderedSections;

    public PathSections(List<Section> orderedSections) {
        this.orderedSections = orderedSections;
    }

    public int getTotalDistance() {
        return orderedSections.stream()
                .map(Section::getDistance)
                .reduce(0, Integer::sum);
    }
}
