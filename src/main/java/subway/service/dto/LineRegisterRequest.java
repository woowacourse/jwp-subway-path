package subway.service.dto;

public class LineRegisterRequest {

    private int distance;
    private String currentStation;
    private String nextStation;
    private String lineName;

    public int getDistance() {
        return distance;
    }

    public String getCurrentStation() {
        return currentStation;
    }

    public String getNextStation() {
        return nextStation;
    }

    public String getLineName() {
        return lineName;
    }
}
