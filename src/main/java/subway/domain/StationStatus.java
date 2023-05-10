package subway.domain;

public enum StationStatus {

    UP,
    MID,
    DOWN;

    public boolean isEqual(final StationStatus status) {
        return this == status;
    }
}
