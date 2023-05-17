package subway.fixture;

import subway.domain.vo.Distance;

public abstract class DistanceFixture {

    public static Distance 거리(final int value) {
        return Distance.from(value);
    }
}
