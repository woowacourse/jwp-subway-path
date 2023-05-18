package subway.dto;

public class PathRequest {

    private final String departureLine;
    private final String departureStation;
    private final String arrivalLine;
    private final String arrivalStation;

    public PathRequest(String departureLine, String departureStation, String arrivalLine, String arrivalStation) {
        this.departureLine = departureLine;
        this.departureStation = departureStation;
        this.arrivalLine = arrivalLine;
        this.arrivalStation = arrivalStation;
    }

    public String getDepartureLine() {
        return departureLine;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public String getArrivalLine() {
        return arrivalLine;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }
}
