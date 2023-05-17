package subway.domain;

import java.util.List;

public class Route {
    
    final private List<Long> stationIds;
    final private int distance;
    
    public Route(final List<Long> stationIds, final int distance) {
        this.stationIds = stationIds;
        this.distance = distance;
    }
    
    public int calculateFare(final FarePolicy farePolicy) {
        return farePolicy.calculateFare(this.distance);
    }
    
    public List<Long> getStationIds() {
        return this.stationIds;
    }
    
    public int getDistance() {
        return this.distance;
    }
    
}
