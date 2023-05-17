package subway.domain.section.general;

import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.section.Section;

public class GeneralSection extends Section {

    private final Long id;
    private final Line line;
    private final Distance distance;

    public GeneralSection(final Long id, final NearbyStations nearbyStations,
                          final Line line, final Distance distance) {
        super(nearbyStations);
        this.id = id;
        this.line = line;
        this.distance = distance;
    }

    public boolean isSameLineId(Long lineId) {
        return line.getId().equals(lineId);
    }

    @Override
    public boolean isTransferSection() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
