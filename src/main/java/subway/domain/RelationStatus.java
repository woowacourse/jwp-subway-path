package subway.domain;

public enum RelationStatus {

    UP,
    DOWN;

    public RelationStatus reverse() {
        if (this == RelationStatus.UP) {
            return DOWN;
        }
        return UP;
    }

    public boolean isUp() {
        return this == UP;
    }
}
