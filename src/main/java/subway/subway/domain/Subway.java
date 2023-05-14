package subway.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.line.domain.Line;
import subway.section.domain.Direction;

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
    
    public void addLine(final String line, final String lineColor) {
        lines.add(new Line(line, lineColor));
    }
    
    public void removeLine(final String lineName) {
        lines.removeIf(line -> line.isSameName(lineName));
    }
    
    public void initAddStation(
            final String line,
            final String leftAdditional,
            final String rightAdditional,
            final long distance
    ) {
        getLineContainStation(line).initAddStation(leftAdditional, rightAdditional, distance);
    }
    
    public void addStation(
            final String line,
            final String base,
            final Direction direction,
            final String additionalStation,
            final long distance
    ) {
        getLineContainStation(line).addStation(base, direction, additionalStation, distance);
    }
    
    public void removeStation(final String line, final String station) {
        getLineContainStation(line).removeStation(station);
    }
    
    private Line getLineContainStation(final String lineName) {
        return lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }
}
