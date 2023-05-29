package subway.domain.fare;

import subway.domain.line.Line;

import java.util.Set;

public class LineSurchargeFarePolicy implements FarePolicy {

    private static final int ZERO = 0;

    @Override
    public int calculateFare(int distance, Set<Line> linesToUse) {
        int maxSurcharge = linesToUse.stream()
                .mapToInt(Line::getSurcharge)
                .filter(line -> line > 0)
                .max()
                .orElse(0);
        return ZERO + maxSurcharge;
    }
}
