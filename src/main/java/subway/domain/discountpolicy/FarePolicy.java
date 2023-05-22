package subway.domain.discountpolicy;

import subway.domain.Line;

import java.util.List;

public interface FarePolicy {
    int calculateFare(final List<Line> boardingLines, final int distance, final int age, final int fare);
}
