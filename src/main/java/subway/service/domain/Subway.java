package subway.service.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Subway {

    private final List<Line> lines;

    public Subway(List<Line> lines) {
        this.lines = lines;
    }

    public List<SingleLine> getAllLine() {
        return lines.stream()
                .map(this::getSingleLine)
                .collect(Collectors.toList());
    }

    public SingleLine getSingleLine(Long lineId) {
        return lines.stream()
                .filter(line -> line.getLineProperty().getId() == lineId)
                .map(this::getSingleLine)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("조회하려는 노선이 존재하지 않습니다."));
    }

    private SingleLine getSingleLine(Line line) {
        RouteMap lineMap = line.getLineMap();
        return SingleLine.of(line.getLineProperty(), lineMap.getStationsOnLine());
    }

}
