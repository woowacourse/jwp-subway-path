package subway.domain.fare;

import subway.domain.line.Line;
import subway.domain.path.Path;

import java.util.Set;

public class LineSurchargeFarePolicy implements FarePolicy {

    private static final int ZERO = 0;

    @Override
    public int calculateFare(Path path) {
        Set<Line> linesToUse = path.getLinesToUse();
        int maxSurcharge = linesToUse.stream()
                .mapToInt(Line::getSurcharge)
                .filter(lineSurcharge -> lineSurcharge > ZERO)
                .max()
                .orElse(ZERO);
        return ZERO + maxSurcharge;
    }
}
