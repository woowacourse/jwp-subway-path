package subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import subway.domain.core.Distance;
import subway.domain.core.Section;

public class PathFindResult {

    private final Distance distance;
    private final List<SectionEdge> path;

    public PathFindResult(final Distance distance, final List<SectionEdge> path) {
        this.distance = distance;
        this.path = path;
    }

    public List<List<Section>> toSections() {
        final List<List<Section>> result = new ArrayList<>();

        List<Section> currentLine = new ArrayList<>();
        long currentLineId = 0;
        for (SectionEdge sectionEdge : path) {
            if (currentLine.isEmpty() || currentLineId != sectionEdge.getLineId()) {
                currentLine = new ArrayList<>();
                currentLineId = sectionEdge.getLineId();
                result.add(currentLine);
            }
            currentLine.add(sectionEdge.toSection());
        }
        return result;
    }

    public int getDistanceValue() {
        return distance.getValue();
    }

    public List<SectionEdge> getPath() {
        return path;
    }
}
