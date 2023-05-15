package subway.controller.dto.response;

public class SectionResponse {

    private final long id;
    private final LineResponse line;
    private final int distance;
    private final StationResponse previousStation;
    private final StationResponse nextStation;

    public SectionResponse(final long id,
                           final LineResponse line,
                           final int distance,
                           final StationResponse previousStation,
                           final StationResponse nextStation) {
        this.id = id;
        this.line = line;
        this.distance = distance;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
    }

    public long getId() {
        return id;
    }

    public LineResponse getLine() {
        return line;
    }

    public int getDistance() {
        return distance;
    }

    public StationResponse getPreviousStation() {
        return previousStation;
    }

    public StationResponse getNextStation() {
        return nextStation;
    }

}
