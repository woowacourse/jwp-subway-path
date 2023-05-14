package subway.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.line.domain.Line;

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
        lines.add(new Line(lineName, lineColor));
    }
}
