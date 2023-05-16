package subway.domain.core;

import java.util.List;

public enum Direction {
    LEFT(new AddStationToLeftStrategy()),
    RIGHT(new AddStationToRightStrategy()),
    ;

    private final AddStationStrategy addStationStrategy;

    Direction(final AddStationStrategy addStationStrategy) {
        this.addStationStrategy = addStationStrategy;
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
        addStationStrategy.add(sections, base, additional, distance);
    }
}
