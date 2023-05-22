package subway.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PathRequest {

    @NotNull
    @NotEmpty
    @NotBlank
    private String sourceStationName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String destinationStationName;

    @Positive
    private Integer passengerAge;

    public PathRequest() {
    }

    public PathRequest(String sourceStationName, String destinationStationName, Integer passengerAge) {
        this.sourceStationName = sourceStationName;
        this.destinationStationName = destinationStationName;
        this.passengerAge = passengerAge;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }

    public Integer getPassengerAge() {
        return passengerAge;
    }

}
