package subway.domain.line;

import static subway.exception.ErrorCode.LINE_EXTRA_FARE_RANGE;

import java.util.Objects;
import subway.exception.BadRequestException;

public class LineExtraFare {

    private final static int MIN_FARE = 0;

    private final int fare;

    public LineExtraFare(final int fare) {
        validateRange(fare);
        this.fare = fare;
    }

    private void validateRange(final int fare) {
        if (fare < MIN_FARE) {
            throw new BadRequestException(LINE_EXTRA_FARE_RANGE);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LineExtraFare that = (LineExtraFare) o;
        return fare == that.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    public int fare() {
        return fare;
    }
}
