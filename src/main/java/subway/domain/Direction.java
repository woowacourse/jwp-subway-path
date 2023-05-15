package subway.domain;

import java.util.List;

public enum Direction {
    UP(new UpAddStrategy()),
    DOWN(new DownAddStrategy());

    private AddStrategy addStrategy;

    Direction(AddStrategy addStrategy) {
        this.addStrategy = addStrategy;
    }

    public void add(List<Edge> edges, Station upStation, Station downStation, int distance) {
        addStrategy.activate(edges, upStation, downStation, distance);
    }
}
