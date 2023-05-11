package subway.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class LineResponse {

    private final List<String> stationNames;
    private final String lineName;

    public LineResponse(List<String> stationNames, String lineName) {
        this.stationNames = stationNames;
        this.lineName = lineName;
    }
}
