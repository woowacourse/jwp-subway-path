package subway.line.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.section.domain.Direction;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
public class Subway {
    private final Set<Line> lines;
    
    public Subway() {
        this(new HashSet<>());
    }
    
    public Subway(final Set<Line> lines) {
        this.lines = lines;
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
            final String lineName,
            final String leftAdditional,
            final String rightAdditional,
            final long distance
    ) {
        findLineByLineName(lineName).initAddStation(leftAdditional, rightAdditional, distance);
    }
    
    public Line addStation(
            final String lineName,
            final String base,
            final Direction direction,
            final String additionalStation,
            final long distance
    ) {
        final Line lineByLineName = findLineByLineName(lineName);
        lineByLineName.addStation(base, direction, additionalStation, distance);
        return lineByLineName;
    }
    
    public Line removeStation(final String line, final String station) {
        final Line lineByLineName = findLineByLineName(line);
        lineByLineName.removeStation(station);
        return lineByLineName;
    }
    
    private Line findLineByLineName(final String lineName) {
        return lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }
}
