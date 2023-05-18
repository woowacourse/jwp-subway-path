package subway.domain.path;

import java.util.List;
import subway.domain.Section;
import subway.domain.Sections;

public class Path {

    private final Sections sections;
    private final int totalDistance;

    public Path(final Sections sections, int totalDistance) {
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
