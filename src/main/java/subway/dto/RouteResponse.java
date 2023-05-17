package subway.dto;

import java.util.List;

public class RouteResponse {
    
    private final List<StationResponse> stations;
    private final int distance;
    
    private RouteResponse(final List<StationResponse> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }
    
    public static RouteResponse of(final List<StationResponse> stationResponses, final int distance) {
        return new RouteResponse(stationResponses, distance);
    }
    
    public List<StationResponse> getStations() {
        return this.stations;
    }
    
    public int getDistance() {
        return this.distance;
    }
    
    
}
