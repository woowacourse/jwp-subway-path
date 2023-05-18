package subway.fixture;

import subway.domain.vo.Distance;

public abstract class DistanceFixture {

    public static Distance 거리(final int 거리) {
        return Distance.from(거리);
    }
}
