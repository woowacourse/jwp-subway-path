package subway.dto;

public class StationRequest {
    private final String name;
    private final CreateType type;
    private final String line;
    private final String previousStation;
    private final String nextStation;
    private final Integer previousDistance;
    private final Integer nextDistance;

    public StationRequest(final String name, final CreateType type, final String line, final String previousStation, final String nextStation, final Integer previousDistance, final Integer nextDistance) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.previousDistance = previousDistance;
        this.nextDistance = nextDistance;
    }

    public String getName() {
        return name;
    }

    public CreateType getType() {
        return type;
    }

    public String getLine() {
        return line;
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
