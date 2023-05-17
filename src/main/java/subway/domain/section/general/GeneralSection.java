package subway.domain.section.general;

import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.section.Section;

public class GeneralSection extends Section {

    private final Line line;
    private final Distance distance;

    public GeneralSection(final Long id, final NearbyStations nearbyStations,
                          final Line line, final Distance distance) {
        super(id, nearbyStations);
        this.line = line;
        this.distance = distance;
    }

    public boolean isSameLineId(Long lineId) {
        return line.getId().equals(lineId);
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
