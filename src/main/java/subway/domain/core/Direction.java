package subway.domain.core;

import java.util.List;

public enum Direction {
    LEFT(new StationAddLeftStrategy()),
    RIGHT(new StationAddRightStrategy()),
    ;

    private final StationAddStrategy stationAddStrategy;

    Direction(final StationAddStrategy stationAddStrategy) {
        this.stationAddStrategy = stationAddStrategy;
    }

    public Direction flip() {
        if (this == LEFT) {
            return RIGHT;
        }
        return LEFT;
    }

    public void addStation(
            final List<Section> sections,
            final Station base,
            final Station additional,
            final Distance distance
    ) {
        stationAddStrategy.add(sections, base, additional, distance);
    }
}
