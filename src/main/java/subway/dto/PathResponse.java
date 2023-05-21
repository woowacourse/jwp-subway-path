package subway.dto;

import subway.domain.path.Path;
import subway.domain.path.PathEdgeProxy;

public final class PathResponse {
    private final Long id;
    private final StationResponse upStation;
    private final StationResponse downStation;
    private final int distance;

    public PathResponse(final Long id, final StationResponse upStation, final StationResponse downStation, final int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static PathResponse from(final Path path) {
        final StationResponse up = StationResponse.of(path.getUp());
        final StationResponse down = StationResponse.of(path.getDown());
        return new PathResponse(path.getId(), up, down, path.getDistance());
    }

    public static PathResponse from(final PathEdgeProxy path) {
        final StationResponse up = StationResponse.of(path.getSource());
        final StationResponse down = StationResponse.of(path.getTarget());
        return new PathResponse(path.getId(), up, down, path.getDistance());
    }

    public Long getId() {
        return id;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
