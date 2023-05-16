package subway.domain.line;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Direction {

    LEFT("LEFT"), RIGHT("RIGHT");

    final String direction;

    Direction(String direction) {
        this.direction = direction;
    }

    public static Direction of(String direction) {
        for (Direction value : Direction.values()) {
            if (value.getDirection().equalsIgnoreCase(direction)) {
                return value;
            }
        }

        throw new IllegalArgumentException("지원하는 방향은 " + Arrays.stream(Direction.values()).sequential().map(Direction::getDirection).collect(Collectors.joining(", ")) + "입니다.");
    }

    public String getDirection() {
        return direction;
    }
}
