package subway.domain.fee;

import java.util.List;
import subway.domain.Line;

public class FeeInformation {

    private final int distance;
    private List<Line> lines;

    public FeeInformation(int distance) {
        this.distance = distance;
    }

    public FeeInformation(int distance, List<Line> lines) {
        this.distance = distance;
        this.lines = lines;
    }

    public int getDistance() {
        return distance;
    }

    public List<Line> getLines() {
        return lines;
    }
}
