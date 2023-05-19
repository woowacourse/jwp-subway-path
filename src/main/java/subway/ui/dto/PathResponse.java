package subway.ui.dto;

import subway.service.dto.PathDto;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final int distance;
    private final int fare;
    private final List<LineResponse> path;

    private PathResponse(final int distance, final int fare, final List<LineResponse> path) {
        this.distance = distance;
        this.fare = fare;
        this.path = path;
    }

    public static PathResponse from(final PathDto pathDto) {
        final List<LineResponse> path = pathDto.getPath().stream()
                .map(LineResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new PathResponse(
                pathDto.getDistance(),
                pathDto.getFare(),
                path
        );
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public List<LineResponse> getPath() {
        return path;
    }
}
