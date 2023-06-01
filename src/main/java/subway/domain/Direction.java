package subway.domain;

import subway.domain.addpathstrategy.AddDownPath;
import subway.domain.addpathstrategy.AddPathStrategy;
import subway.domain.addpathstrategy.AddUpPath;

import java.util.Arrays;

public enum Direction {
    UP(new AddUpPath()),
    DOWN(new AddDownPath());

    private final AddPathStrategy addPathStrategy;

    Direction(final AddPathStrategy addPathStrategy) {
        this.addPathStrategy = addPathStrategy;
    }

    public static Direction of(final String string) {
        return Arrays.stream(Direction.values())
                .filter(direction -> direction.name().equalsIgnoreCase(string))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));
    }

    public AddPathStrategy getStrategy() {
        return addPathStrategy;
    }
}
