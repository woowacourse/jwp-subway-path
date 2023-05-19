package subway.domain.line;

import java.util.List;
import subway.domain.Distance;

public enum Direction {
    UP(new UpAddStrategy()),
    DOWN(new DownAddStrategy());

    private AddStrategy addStrategy;

    Direction(AddStrategy addStrategy) {
        this.addStrategy = addStrategy;
    }

    public void add(List<Section> sections, Station upStation, Station downStation, Distance distance) {
        addStrategy.activate(sections, upStation, downStation, distance);
    }
}
