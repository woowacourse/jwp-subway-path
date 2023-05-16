package subway.fixture;

import subway.domain.Distance;

public abstract class DistanceFixture {

    public static Distance 거리(final int value) {
        return new Distance(value);
    }
}
