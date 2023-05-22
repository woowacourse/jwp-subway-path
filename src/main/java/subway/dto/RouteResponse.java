package subway.dto;

import java.util.List;

public final class RouteResponse {
    
    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;
    
    private RouteResponse(final List<StationResponse> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }
    
    public static RouteResponse of(final List<StationResponse> stationResponses, final int distance, final int fare) {
        return new RouteResponse(stationResponses, distance, fare);
    }
    
    public List<StationResponse> getStations() {
        return this.stations;
    }
    
    public int getDistance() {
        return this.distance;
    }
    
    public int getFare() {
        return this.fare;
    }
}
