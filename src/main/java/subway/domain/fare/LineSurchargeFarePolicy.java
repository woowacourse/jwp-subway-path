package subway.domain.fare;

import subway.domain.line.Line;

import java.util.Set;

public class LineSurchargeFarePolicy implements FarePolicy {

    private static final int ZERO = 0;

    @Override
    public int calculateFare(int distance, Set<Line> linesToUse) {
        int maxSurcharge = linesToUse.stream()
                .mapToInt(Line::getSurcharge)
                .filter(lineSurcharge -> lineSurcharge > ZERO)
                .max()
                .orElse(ZERO);
        return ZERO + maxSurcharge;
    }
}
