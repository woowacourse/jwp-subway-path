package subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Subway {

    private final List<Line> lines;

    public Subway(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public void addLine(Line newLine) {
        validateDuplicatedName(newLine.getName());
        validateDuplicatedColor(newLine.getColor());
        lines.add(newLine);
    }

    private void validateDuplicatedName(String name) {
        boolean hasDuplicatedName = lines.stream()
                .anyMatch(line -> line.hasSameName(name));
        if (hasDuplicatedName) {
            throw new IllegalArgumentException("노선의 이름은 중복될 수 없습니다.");
        }
    }

    private void validateDuplicatedColor(String color) {
        boolean hasDuplicatedColor = lines.stream()
                .anyMatch(line -> line.hasSameColor(color));
        if (hasDuplicatedColor) {
            throw new IllegalArgumentException("노선의 색상은 중복될 수 없습니다.");
        }
    }

    public void removeLine(Line line) {
        if (!lines.contains(line)) {
            throw new IllegalArgumentException("해당 역이 존재하지 않습니다.");
        }
        lines.remove(line);
    }
    
    public void updateLineName(Long lineId, String newName) {
        Line oldLine = findLineById(lineId);
        if (oldLine.hasSameName(newName)) {
            return;
        }
        validateDuplicatedName(newName);
        lines.remove(oldLine);
        lines.add(new Line(lineId, newName, oldLine.getColor(), oldLine.getCharge(), new Sections(oldLine.getSections())));
    }

    public void updateLineColor(Long lineId, String newColor) {
        Line oldLine = findLineById(lineId);
        if (!oldLine.hasSameColor(newColor)) {
            validateDuplicatedColor(newColor);
        }
        lines.remove(oldLine);
        lines.add(new Line(lineId, oldLine.getName(), newColor, oldLine.getCharge(), new Sections(oldLine.getSections())));
    }
    
    public void updateLineCharge(Long lineId, int charge) {
        Line oldLine = findLineById(lineId);
    
        lines.remove(oldLine);
        lines.add(new Line(lineId, oldLine.getName(), oldLine.getColor(), charge, new Sections(oldLine.getSections())));
    }

    public void initLine(Long lineId, Station upStation, Station downStation, Distance distance) {
        Line line = findLineById(lineId);
        checkIfExistSection(upStation, downStation, distance);
        line.addInitialStations(upStation, downStation,distance);
    }

    private void checkIfExistSection(Station upStation, Station downStation, Distance distance) {
        if(lines.stream()
                .anyMatch(savedLine -> savedLine.hasSameSection(new Section(upStation, downStation, distance)))) {
            throw new IllegalArgumentException("지하철에 이미 존재하는 구간입니다.");
        }
    }

    public void addStationToLine(Long lineId, Station upStation, Station downStation, Direction directionOfBase,
            Distance distance) {
        Line line = findLineById(lineId);
        checkIfExistSection(upStation, downStation, distance);
        line.addStation(upStation, downStation, directionOfBase, distance);
    }

    public Line findLineById(Long id) {
        return lines.stream()
                .filter(line -> line.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 노선이 존재하지 않습니다."));
    }

    public List<Station> findAllStations() {
        return lines.stream()
                .map(Line::findStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public Station findStationByName(String name) {
        return findAllStations().stream()
                .filter(station -> station.hasSameName(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 존재하지 않습니다"));
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }

}
