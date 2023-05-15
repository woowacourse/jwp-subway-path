package subway.domain;

import java.util.List;
import subway.domain.strategy.AddStationStrategy;
import subway.domain.strategy.AddStationToLeftStrategy;
import subway.domain.strategy.AddStationToRightStrategy;

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
