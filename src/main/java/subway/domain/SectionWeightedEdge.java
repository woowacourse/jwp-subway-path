package subway.domain;

import static java.util.stream.Collectors.*;

import java.util.List;

public class SectionWeightedEdge {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Line line;
    private final int distance;

    public SectionWeightedEdge(Long id, Station upStation, Station downStation, Line line, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
        this.distance = distance;
    }

    public static List<SectionWeightedEdge> toSectionWeightedEdges(Sections sections) {
        return sections.getSections().stream()
            .map(SectionWeightedEdge::of)
            .collect(toList());
    }

    private static SectionWeightedEdge of(Section section) {
        return new SectionWeightedEdge(section.getId(), section.getUpStation(), section.getDownStation(),
            section.getLine(), section.getDistance());
    }

    public Section toSection() {
        return new Section(id, upStation, downStation, line, distance);
    }

    protected Station getSource() {
        return upStation;
    }

    protected Station getTarget() {
        return downStation;
    }

    protected double getWeight() {
        return distance;
    }
}
