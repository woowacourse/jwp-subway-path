package subway.domain;

import java.util.Arrays;

public enum Direction {

    UP("UP", new AddUpStationStrategy()),
    DOWN("DOWN", new AddDownStationStrategy());

    private final String value;
    private final AddStationStrategy addStationStrategy;

    Direction(String value, AddStationStrategy addStationStrategy) {
        this.value = value;
        this.addStationStrategy = addStationStrategy;
    }

    public boolean isSameDirection(String input) {
        return this.value.equals(input);
    }

    public static Direction findDirection(String input) {
        return Arrays.stream(values())
                .filter(direction -> direction.isSameDirection(input))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("일치하는 방향이 존재하지 않습니다."));
    }

    public AddStationStrategy findAddStrategy() {
        return this.addStationStrategy;
    }
}
