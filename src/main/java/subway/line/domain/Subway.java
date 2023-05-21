package subway.line.domain;

import subway.line.dto.GetAllSortedLineResponse;
import subway.line.dto.GetSortedLineResponse;
import subway.section.domain.Direction;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    
    public List<String> getSortedStations(final String lineName) {
        return findLineByLineName(lineName).getSortedStations();
    }
    
    public GetAllSortedLineResponse getAllSortedStations() {
        return new GetAllSortedLineResponse(
                lines.stream()
                        .map(line -> new GetSortedLineResponse(line.getName(), line.getColor(), getSortedStations(line.getName())))
                        .collect(Collectors.toUnmodifiableList())
        );
    }
    
    public Set<Line> removeStationOnAllLine(final String stationName) {
        final Set<Line> linesWithStation = getLinesWithStation(stationName);
        validateExistStation(linesWithStation);
        linesWithStation.forEach(line -> line.removeStation(stationName));
        return linesWithStation;
    }
    
    private void validateExistStation(final Set<Line> linesWithStation) {
        if (linesWithStation.isEmpty()) {
            throw new IllegalArgumentException("stationId에 해당하는 역이 모든 노선에 존재하지 않습니다.");
        }
    }
    
    private Set<Line> getLinesWithStation(final String stationName) {
        return lines.stream()
                .filter(line -> line.isContainsStation(stationName))
                .collect(Collectors.toUnmodifiableSet());
    }
    
    public Set<Line> getLines() {
        return lines;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Subway subway = (Subway) o;
        return Objects.equals(lines, subway.lines);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
    
    @Override
    public String toString() {
        return "Subway{" +
                "lines=" + lines +
                '}';
    }
}
