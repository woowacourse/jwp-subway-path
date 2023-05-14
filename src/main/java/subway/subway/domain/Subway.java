package subway.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.line.domain.Line;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
public class Subway {
    private final Set<Line> lines;
    
    public Subway() {
        this.lines = new HashSet<>();
    }
    
    public void addLine(final String lineName, final String lineColor) {
        lines.add(new Line(lineName, lineColor));
    }
    
    public void removeLine(final String lineName) {
        lines.removeIf(line -> line.isSameName(lineName));
    }
    
    public void initAddStation(
            final String lineName,
            final String leftAdditional,
            final String rightAdditional,
            final long distance
    ) {
        final Line line = getLineContainStation(lineName);
        line.initAddStation(leftAdditional, rightAdditional, distance);
    }
    
    private Line getLineContainStation(final String lineName) {
        return lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }
}
