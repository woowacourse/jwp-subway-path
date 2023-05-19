package subway.domain.interstation.add;

import java.util.Arrays;
import java.util.List;
import subway.domain.interstation.InterStation;
import subway.domain.interstation.exception.InterStationsException;

public enum AddInterStationPolicy {
    ADD_FIRST(new AddFirstInterStationStrategy()),
    ADD_LAST(new AddMiddleInterStationStrategy()),
    ADD_MIDDLE(new AddLastInterStationStrategy());

    private final AddInterStationStrategy addInterStationStrategy;

    AddInterStationPolicy(final AddInterStationStrategy addInterStationStrategy) {
        this.addInterStationStrategy = addInterStationStrategy;
    }

    public static AddInterStationStrategy of(final List<InterStation> interStations,
            final Long upStationId,
            final Long downStationId,
            final Long newStationId) {
        return Arrays.stream(values())
                .map(it -> it.addInterStationStrategy)
                .filter(it -> it.isSatisfied(interStations, upStationId, downStationId, newStationId))
                .findAny()
                .orElseThrow(() -> new InterStationsException("역을 추가할 수 없습니다"));
    }
}
