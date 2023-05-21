package subway.domain;

import subway.domain.section.Distance;

import java.util.List;

public class Path {

    private final List<SectionEdge> sectionEdges;
    private final Distance distance;

    public Path(final List<SectionEdge> sectionEdges, final Distance distance) {
        this.sectionEdges = sectionEdges;
        this.distance = distance;
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Path{" +
                "specificSections=" + sectionEdges +
                ", distance=" + distance +
                '}';
    }
}
