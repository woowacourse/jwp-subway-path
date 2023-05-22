package subway.dto;

import javax.validation.constraints.NotBlank;

public class PathRequest {

    @NotBlank(message = "출발노선은 빈칸이 되면 안됩니다.")
    private String departureLine;

    @NotBlank(message = "출발역은 빈칸이 되면 안됩니다.")
    private String departureStation;

    @NotBlank(message = "도착노선은 빈칸이 되면 안됩니다.")
    private String arrivalLine;

    @NotBlank(message = "도착역은 빈칸이 되면 안됩니다.")
    private String arrivalStation;

    public PathRequest() {
    }

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
