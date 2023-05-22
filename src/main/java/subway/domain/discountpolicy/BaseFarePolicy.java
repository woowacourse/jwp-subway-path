package subway.domain.discountpolicy;

import subway.domain.Line;

import java.util.List;

public class BaseFarePolicy implements FarePolicy {

    @Override
    public int calculateFare(final List<Line> boardingLines, final int distance, final int age, final int fare) {
        int total = fare + getMaxSurCharge(boardingLines);
        total += Math.max(distance - 50, 0) / 8L * 100;
        total += Math.max(distance - 10, 0) / 5L * 100;
        return total;
    }

    private int getMaxSurCharge(final List<Line> boardingLines) {
        return boardingLines.stream()
                .mapToInt(Line::getSurcharge)
                .max()
                .orElseThrow(IllegalArgumentException::new);
    }
}
