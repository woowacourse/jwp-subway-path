package subway.dto;

public class LineStationRequest {
    private String name;
    private String createType;
    private String line;
    private boolean upEndpoint;
    private boolean downEndpoint;
    private String previousStation;
    private Integer previousDistance;
    private String nextStation;
    private Integer nextDistance;

    public LineStationRequest(String name, String createType, String line, boolean upEndpoint, boolean downEndpoint, String previousStation, String nextStation, Integer previousDistance, Integer nextDistance) {
        this.name = name;
        this.createType = createType;
        this.line = line;
        this.upEndpoint = upEndpoint;
        this.downEndpoint = downEndpoint;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.previousDistance = previousDistance;
        this.nextDistance = nextDistance;
    }

    public String getName() {
        return name;
    }

    public String getCreateType() {
        return createType;
    }

    public String getLine() {
        return line;
    }

    public boolean isUpEndpoint() {
        return upEndpoint;
    }

    public boolean isDownEndpoint() {
        return downEndpoint;
    }

    public String getPreviousStation() {
        return previousStation;
    }

    public String getNextStation() {
        return nextStation;
    }

    public Integer getPreviousDistance() {
        return previousDistance;
    }

    public Integer getNextDistance() {
        return nextDistance;
    }
}
