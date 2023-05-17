package subway.domain;

public enum Side {

    LEFT, RIGHT;

    public boolean isRight() {
        return this == RIGHT;
    }
}
