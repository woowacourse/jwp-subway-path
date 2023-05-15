package subway.ui.dto.request;

public class AddStationRequest {
    private final Long firstStationId;
    private final Long secondStationId;
    private final String newStation;
    private final int distance;

    public AddStationRequest(
            final Long firstStationId,
            final Long secondStationId,
            final String newStation,
            final int distance
    ) {
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
        this.newStation = newStation;
        this.distance = distance;
    }

    public Long getFirstStationId() {
        return firstStationId;
    }

    public Long getSecondStationId() {
        return secondStationId;
    }

    public String getNewStation() {
        return newStation;
    }

    public int getDistance() {
        return distance;
    }
}
