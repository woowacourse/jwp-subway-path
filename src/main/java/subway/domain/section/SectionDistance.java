package subway.domain.section;

import static subway.exception.ErrorCode.SECTION_DISTANCE;

import subway.exception.BadRequestException;

public class SectionDistance {

    private static final int MIN_DISTANCE = 1;
    private static final int MAX_DISTANCE = 50;

    private final int distance;

    private SectionDistance(final int distance) {
        this.distance = distance;
    }

    public static SectionDistance create(final int distance) {
        validateRange(distance);
        return new SectionDistance(distance);
    }

    public static SectionDistance zero() {
        return new SectionDistance(0);
    }

    public SectionDistance add(final SectionDistance distance) {
        return new SectionDistance(this.distance + distance.distance);
    }

    public SectionDistance subtract(final SectionDistance distance) {
        return new SectionDistance(this.distance - distance.distance);
    }

    public boolean isGreaterAndEqualsThan(final SectionDistance distance) {
        return this.distance >= distance.distance;
    }

    private static void validateRange(final int distance) {
        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
            throw new BadRequestException(SECTION_DISTANCE);
        }
    }

    public int getDistance() {
        return distance;
    }
}
