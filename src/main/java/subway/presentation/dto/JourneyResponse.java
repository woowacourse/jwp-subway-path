package subway.presentation.dto;

import java.util.List;

public class JourneyResponse {

    private final List<String> path;
    private final Double distance;
    private final Integer fare;

    public JourneyResponse(List<String> path, Double distance, Integer fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public List<String> getPath() {
        return path;
    }

    public Double getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
