package subway.service.dto.response;

public class SectionInLineResponse {

    private final String currentStationName;
    private final String nextStationName;
    private final int distance;

    public SectionInLineResponse(final String currentStationName, final String nextStationName, final int distance) {
        this.currentStationName = currentStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
    }

    public String getCurrentStationName() {
        return currentStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public int getDistance() {
        return distance;
    }
}
