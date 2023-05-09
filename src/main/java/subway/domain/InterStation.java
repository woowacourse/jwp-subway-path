package subway.domain;

import subway.exception.BusinessException;

public class InterStation {

    private final Station start;
    private final Station end;
    private final long distance;

    public InterStation(final Station start, final Station end, final long distance) {
        validateDistance(distance);
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    private void validateDistance(final long distance) {
        if (distance < 0) {
            throw new BusinessException("거리는 양수이어야 합니다.");
        }
    }
}
