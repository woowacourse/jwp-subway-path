package subway.line.domain.navigation.domain;

import subway.line.domain.section.Section;

import java.util.List;

public interface SubwayGraph {
    default void initialize(List<List<Section>> sectionsOfAllLines) {
        for (List<Section> sections : sectionsOfAllLines) {
            initializeSection(sections);
        }
    }

    private void initializeSection(List<Section> sections) {
        for (Section section : sections) {
            this.addPath(section);
        }
    }

    void addPath(Section section);

    Path makePath();
}
