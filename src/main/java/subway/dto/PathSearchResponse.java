package subway.dto;

import subway.domain.Fare;
import subway.domain.Path;

public class PathSearchResponse {

    private final Path path;
    private final Fare fare;

    public PathSearchResponse(final Path path, final Fare fare) {
        this.path = path;
        this.fare = fare;
    }

    public Path getPath() {
        return path;
    }

    public Fare getFare() {
        return fare;
    }
}
