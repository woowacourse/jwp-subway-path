package subway.dto;

public class SectionResponse {

    private final StationResponse fromStation;
    private final StationResponse toStation
            ;
    private final LineResponse line;
    private final Integer distance;

    public SectionResponse(final StationResponse fromStation, final StationResponse toStatiion, final LineResponse line, final Integer distance) {
        this.fromStation = fromStation;
        this.toStation = toStatiion;
        this.line = line;
        this.distance = distance;
    }

    public StationResponse getFromStation() {
        return fromStation;
    }

    public StationResponse getToStation() {
        return toStation;
    }

    public LineResponse getLine() {
        return line;
    }

    public Integer getDistance() {
        return distance;
    }
}
