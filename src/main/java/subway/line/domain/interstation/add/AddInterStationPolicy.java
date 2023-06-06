package subway.line.domain.interstation.add;

import java.util.Arrays;
import java.util.List;
import subway.line.domain.interstation.InterStation;
import subway.line.domain.interstation.exception.InterStationsException;

public enum AddInterStationPolicy {
    ADD_FIRST(new AddFirstInterStationStrategy()),
    ADD_LAST(new AddMiddleInterStationStrategy()),
    ADD_MIDDLE(new AddLastInterStationStrategy());

    private final AddInterStationStrategy addInterStationStrategy;

    AddInterStationPolicy(AddInterStationStrategy addInterStationStrategy) {
        this.addInterStationStrategy = addInterStationStrategy;
    }

    public static AddInterStationStrategy of(List<InterStation> interStations,
            Long upStationId,
            Long downStationId,
            Long newStationId) {
        return Arrays.stream(values())
                .map(it -> it.addInterStationStrategy)
                .filter(it -> it.isSatisfied(interStations, upStationId, downStationId, newStationId))
                .findAny()
                .orElseThrow(() -> new InterStationsException("역을 추가할 수 없습니다"));
    }
}
