package subway.domain;

import subway.exception.InvalidInputException;

import java.util.List;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public boolean isInitialAdd(long lineId) {
        return lines.stream().anyMatch(line ->
                line.getId() == lineId && line.isBlank()
        );
    }

    public Line findLineBy(long lineId) {
        return lines.stream()
                .filter(line -> line.getId() == lineId)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("존재하지 않는 라인입니다."));
    }

}
