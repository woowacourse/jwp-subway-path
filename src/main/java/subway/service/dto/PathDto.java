package subway.service.dto;

import java.util.List;

public class PathDto {

    final private int distance;
    final private int fare;
    final private List<LineDto> path;

    public PathDto(final int distance, final int fare, final List<LineDto> path) {
        this.distance = distance;
        this.fare = fare;
        this.path = path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public List<LineDto> getPath() {
        return path;
    }
}
