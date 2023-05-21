package subway.domain.path;

import subway.domain.Sections;

public class Path {

    private final Sections sections;
    private final int totalDistance;

    public Path(final Sections sections, int totalDistance) {
        this.sections = sections;
        this.totalDistance = totalDistance;
    }

    public Sections getSections() {
        return sections;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
