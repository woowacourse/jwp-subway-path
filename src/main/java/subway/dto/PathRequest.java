package subway.dto;

public class PathRequest {

    private final Long fromStation;
    private final Long toStation;

    public PathRequest(Long fromStation, Long toStation) {
        this.fromStation = fromStation;
        this.toStation = toStation;
    }

    public Long getFromStation() {
        return fromStation;
    }

    public Long getToStation() {
        return toStation;
    }
}
