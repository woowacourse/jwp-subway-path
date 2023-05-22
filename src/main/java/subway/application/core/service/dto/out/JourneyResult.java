package subway.application.core.service.dto.out;

import subway.application.core.domain.Station;

import java.util.List;

public class JourneyResult {

    private final List<StationResult> path;
    private final Double distance;
    private final Integer fare;

    public JourneyResult(List<StationResult> path, Double distance, Integer fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResult> getPath() {
        return path;
    }

    public Double getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
