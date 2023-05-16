package subway.application.strategy.insert;

import subway.domain.Distance;
import subway.domain.Sections;

public interface InsertStrategyInterface {

    boolean support(Sections sections, InsertSection insertSection);

    Long insert(Sections sections, InsertSection insertSection);

    default void validateDistance(Distance targetDistance, Distance distance) {
        if (targetDistance.isShorterThanAndEqual(distance)) {
            throw new IllegalArgumentException("거리는 기존 구간 거리보다 클 수 없습니다.");
        }
    }
}
