package subway.domain.section;

import subway.exception.ErrorCode;
import subway.exception.GlobalException;

public class SectionDistance {

    private static final int MIN_DISTANCE = 1;
    private static final int MAX_DISTANCE = 50;

    private final int distance;

    public SectionDistance(final int distance) {
        validateRange(distance);
        this.distance = distance;
    }

    private void validateRange(final int distance) {
        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
            throw new GlobalException(ErrorCode.SECTION_DISTANCE);
        }
    }

    public int getDistance() {
        return distance;
    }
}
