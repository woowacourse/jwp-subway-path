package subway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import subway.domain.Station;

import java.util.List;

public class ShortestPathResponse {
    private List<Station> path;
    private int distance;
    private int fare;

    public ShortestPathResponse(List<Station> path, int distance, int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    private ShortestPathResponse() {
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
