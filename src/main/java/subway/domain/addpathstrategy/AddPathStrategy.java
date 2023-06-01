package subway.domain.addpathstrategy;

import subway.domain.Path;
import subway.domain.Station;

import java.util.List;
import java.util.Map;

public interface AddPathStrategy {

    Map<Station, Path> add(
            final Station targetStation,
            final Station addStation,
            final int distance,
            final List<Station> stations,
            final Map<Station, Path> paths
    );

    default void validatePathDistance(final Integer distance, final Path path) {
        if (path.isShorterThan(distance)) {
            throw new IllegalArgumentException("기존 경로보다 짧은 경로를 추가해야 합니다.");
        }
    }
}
