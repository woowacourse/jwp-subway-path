package subway.service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteFindingRequest {

    private final String startStation;
    private final String endStation;

    public RouteFindingRequest(@JsonProperty(value = "startStation") String startStation,
                               @JsonProperty(value = "endStation") String endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

}
