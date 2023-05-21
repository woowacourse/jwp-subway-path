package subway.shortestpathfinder.dto;

import javax.validation.constraints.NotBlank;

public class GetShortestPathRequest {
    @NotBlank(message = "startStationName은 null 또는 empty일 수 없습니다.")
    private String startStationName;
    @NotBlank(message = "endStationName은 null 또는 empty일 수 없습니다.")
    private String endStationName;
    
    public GetShortestPathRequest() {}
    
    public GetShortestPathRequest(final String startStationName, final String endStationName) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
    }
    
    public String getStartStationName() {
        return startStationName;
    }
    
    public String getEndStationName() {
        return endStationName;
    }
    
    @Override
    public String toString() {
        return "GetShortestPathRequest{" +
                "startStationName='" + startStationName + '\'' +
                ", endStationName='" + endStationName + '\'' +
                '}';
    }
}
