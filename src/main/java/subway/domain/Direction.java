package subway.domain;

import subway.exception.InvalidInputException;

public enum Direction {
    UP(false),
    DOWN(true);
    
    private final boolean direction;
    
    Direction(final boolean direction) {
        this.direction = direction;
    }
    
    public static Direction of(final String direction) {
        try {
            return Direction.valueOf(direction.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new InvalidInputException("잘못된 방향입니다. 방향은 UP 또는 DOWN만 가능합니다.");
        }
    }
    
    public boolean isUp() {
        return !this.direction;
    }
    
    public boolean isDown() {
        return this.direction;
    }
}
