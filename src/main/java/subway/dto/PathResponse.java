package subway.dto;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private final List<PathSectionDto> pathSections;
    private final int totalDistance;
    private final int fare;

    public PathResponse(final List<PathSectionDto> pathSections, final int totalDistance,
                        final int fare) {
        this.pathSections = new ArrayList<>(pathSections);
        this.totalDistance = totalDistance;
        this.fare = fare;
    }

    public List<PathSectionDto> getPathSections() {
        return pathSections;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getFare() {
        return fare;
    }
}
