package subway.domain;

import java.util.List;
import subway.domain.strategy.AddStationLeftStrategy;
import subway.domain.strategy.AddStationRightStrategy;
import subway.domain.strategy.AddStationStrategy;

public enum Direction {
    LEFT(new AddStationLeftStrategy()),
    RIGHT(new AddStationRightStrategy()),
    ;

    private final AddStationStrategy addStationStrategy;

    Direction(AddStationStrategy addStationStrategy) {
        this.addStationStrategy = addStationStrategy;
    }

    public void addStation(
            final List<Section> sections,
            final Station base,
            final Station additional,
            final Distance distance
    ) {
        addStationStrategy.addStation(sections, base, additional, distance);
    }
}
