package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AddStationRequest {

    @NotBlank(message = "출발역은 공백이면 안됩니다.")
    private String departureStation;
    @NotBlank(message = "도착역은 공백이면 안됩니다.")
    private String arrivalStation;
    @NotNull
    @Positive(message = "거리는 양의 정수여야 합니다.")
    private int distance;

    public AddStationRequest() {
    }


    public AddStationRequest(final String departureStation, final String arrivalStation, final int distance) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.distance = distance;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public int getDistance() {
        return distance;
    }
}
