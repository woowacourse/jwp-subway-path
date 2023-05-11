package subway.dto;

import lombok.Getter;

@Getter
public class DeleteStationRequest {

    private String lineName;
    private String stationName;

    public DeleteStationRequest(String lineName, String stationName) {
        this.lineName = lineName;
        this.stationName = stationName;
    }
}
