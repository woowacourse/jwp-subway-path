package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import subway.exception.DuplicatedNameException;
import subway.exception.line.NonExistLineException;

public class Subway {

    private final List<Line> lines;

    public Subway() {
        this(new ArrayList<>());
    }

    public Subway(List<Line> lines) {
        this.lines = lines;
    }

    public void addLine(Line line) {
        validateDuplicatedLineName(line);
        lines.add(line);
    }

    private void validateDuplicatedLineName(Line line) {
        if (isDuplicatedName(line)) {
            throw new DuplicatedNameException(line.getName());
        }
    }

    private boolean isDuplicatedName(Line other) {
        return lines.stream().anyMatch(it -> it.isSame(other));
    }

    public List<Line> getLines() {
        return lines;
    }

    public void removeStation(String lineName, Station station) {
        Line line = findLineByName(lineName);
        line.removeStation(station);
        if (line.isEmpty()) {
            lines.remove(line);
        }
    }

    public Line findLineById(final Long lineId) {
        return lines.stream()
                .filter(line -> line.getId().equals(lineId))
                .findFirst()
                .orElseThrow(() -> new NonExistLineException(lineId));
    }

    public Line findLineByName(String lineName) {
        return lines.stream()
                .filter(line -> line.getName().equals(lineName))
                .findAny()
                .orElseThrow(() -> new NonExistLineException(lineName));
    }

    public void addStation(String name, String sourceStation, String targetStation, int distance) {
        Line line = findLineByName(name);
        line.addSection(new Section(sourceStation, targetStation, distance));
    }

    public boolean notContainsStation(final Station station) {
        return lines.stream()
                .noneMatch(line -> line.containsStation(station));
    }

    public List<Station> getAllStations() {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toUnmodifiableList());
    }
}
