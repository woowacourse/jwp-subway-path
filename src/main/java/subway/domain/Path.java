package subway.domain;

import java.util.List;

public class Path {

    private final Sections sections;
    private final int totalDistance;

    public Path(Sections sections, int totalDistance) {
        this.sections = sections;
        this.totalDistance = totalDistance;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
