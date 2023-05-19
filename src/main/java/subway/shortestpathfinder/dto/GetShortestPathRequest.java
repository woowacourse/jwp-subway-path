package subway.shortestpathfinder.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GetShortestPathRequest that = (GetShortestPathRequest) o;
        return Objects.equals(startStationName, that.startStationName) && Objects.equals(endStationName, that.endStationName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(startStationName, endStationName);
    }
    
    @Override
    public String toString() {
        return "GetShortestPathRequest{" +
                "startStationName='" + startStationName + '\'' +
                ", endStationName='" + endStationName + '\'' +
                '}';
    }
}
