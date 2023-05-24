package subway.domain.fare;

import java.util.Set;

import subway.domain.subway.Line;

public class FareInfo {
    private final int distance;
    private final Set<Line> lines;
    private final int age;

    public FareInfo(int distance, Set<Line> lines, int age) {
        this.distance = distance;
        this.lines = lines;
        this.age = age;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public int getAge() {
        return age;
    }
}
