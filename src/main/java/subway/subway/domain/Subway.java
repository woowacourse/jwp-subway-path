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
    
    public void addLine(final String lineName, final String lineColor) {
        if (isExistNameOrColor(lineName, lineColor)) {
            throw new IllegalArgumentException("이미 존재하는 노선의 이름 또는 색상으로는 노선을 추가할 수 없습니다. " +
                    "lineName : " + lineName + ", lineColor : " + lineColor);
        }
        lines.add(new Line(lineName, lineColor));
    }
    
    private boolean isExistNameOrColor(final String lineName, final String lineColor) {
        return lines.stream()
                .anyMatch(line -> line.isSameName(lineName) || line.isSameColor(lineColor));
    }
    
    public void removeLine(final String lineName) {
        lines.remove(findLineByLineName(lineName));
    }
    
    public void initAddStation(
            final String line,
            final String leftAdditional,
            final String rightAdditional,
            final long distance
    ) {
        findLineByLineName(line).initAddStation(leftAdditional, rightAdditional, distance);
    }
    
    public void addStation(
            final String line,
            final String base,
            final Direction direction,
            final String additionalStation,
            final long distance
    ) {
        findLineByLineName(line).addStation(base, direction, additionalStation, distance);
    }
    
    public void removeStation(final String line, final String station) {
        findLineByLineName(line).removeStation(station);
    }
    
    private Line findLineByLineName(final String lineName) {
        return lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }
}
