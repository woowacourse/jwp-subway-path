package subway.dto;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;

public class LineDto {

    private final Line line;
    private final List<Station> allStationsInOrder;

    public LineDto(final Line line, final List<Station> allStationsInOrder) {
        this.line = line;
        this.allStationsInOrder = allStationsInOrder;
    }

    public Line getLine() {
        return line;
    }

    public List<Station> getAllStationsInOrder() {
        return allStationsInOrder;
    }
}
