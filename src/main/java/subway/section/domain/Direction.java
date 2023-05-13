package subway.section.domain;

public enum Direction {
    LEFT, RIGHT;
    
    public boolean isLeft() {
        return this == LEFT;
    }
    
    public boolean isRight() {
        return this == RIGHT;
    }
}
