package subway.domain.interstation.remove;

import java.util.Arrays;
import java.util.List;
import subway.domain.interstation.InterStation;
import subway.domain.interstation.exception.InterStationsException;

public enum RemoveInterStationPolicy {
    REMOVE_FIRST(new RemoveFirstInterStationStrategy()),
    REMOVE_LAST(new RemoveLastInterStationStrategy()),
    REMOVE_MIDDLE(new RemoveMiddleInterStationStrategy());

    private final RemoveInterStationStrategy removeInterStationStrategy;

    RemoveInterStationPolicy(final RemoveInterStationStrategy removeInterStationStrategy) {
        this.removeInterStationStrategy = removeInterStationStrategy;
    }

    public static RemoveInterStationStrategy of(final List<InterStation> interStations, final long removeStationId) {
        return Arrays.stream(values())
                .map(it -> it.removeInterStationStrategy)
                .filter(it -> it.isSatisfied(interStations, removeStationId))
                .findAny()
                .orElseThrow(() -> new InterStationsException("역을 제거할 수 없습니다"));
    }
}
