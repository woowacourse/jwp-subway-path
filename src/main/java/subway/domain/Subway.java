package subway.domain;

import java.util.ArrayList;
import java.util.List;
import subway.domain.routestrategy.RouteStrategy;

public class Subway {

    private final List<Line> lines;

    public Subway(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
        validate(lines);
    }

    private void validate(List<Line> lines) {
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            boolean hasDuplicatedName = lines.subList(i + 1, lines.size())
                    .stream()
                    .anyMatch(other -> line.hasSameName(other.getName()));
            boolean hasDuplicatedColor = lines.subList(i + 1, lines.size())
                    .stream()
                    .anyMatch(other -> line.hasSameColor(other.getColor()));
            if (hasDuplicatedName) {
                throw new IllegalArgumentException("노선의 이름은 중복될 수 없습니다.");
            }
            if (hasDuplicatedColor) {
                throw new IllegalArgumentException("노선의 색상은 중복될 수 없습니다.");
            }
        }
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

    public void addLine(Line newLine) {
        validateDuplicatedName(newLine.getName());
        validateDuplicatedColor(newLine.getColor());
        lines.add(newLine);
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
        lines.add(new Line(lineId, newName, oldLine.getColor(), new Sections(oldLine.getSections())));
    }

    public void updateLineColor(Long lineId, String newColor) {
        Line oldLine = findLineById(lineId);
        if (!oldLine.hasSameColor(newColor)) {
            validateDuplicatedColor(newColor);
        }
        lines.remove(oldLine);
        lines.add(new Line(lineId, oldLine.getName(), newColor, new Sections(oldLine.getSections())));
    }

    //todo : 노선이 라고 에러메시지 바꾸기
    public Line findLineById(Long id) {
        return lines.stream()
                .filter(line -> line.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 존재하지 않습니다."));
    }

    public Line findLineByName(String name) {
        return lines.stream()
                .filter(line -> line.hasSameName(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 존재하지 않습니다."));
    }

    public List<Station> findShortestRoute(Station start, Station end, RouteStrategy strategy) {
        //같은역인지
        if (start.equals(end)) {
            throw new IllegalArgumentException("출발지와 도착지가 같은 역입니다.");
        }
        // 해당역이 지하철 노선 안에 존재하느닞 확인하기
        return strategy.findShortestRoute(lines, start, end);
    }

    public Distance findShortestDistance(Station start, Station end, RouteStrategy strategy) {
        return strategy.findShortestDistance(lines, start, end);
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }

}
