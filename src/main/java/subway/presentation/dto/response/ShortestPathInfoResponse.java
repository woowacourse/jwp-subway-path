package subway.presentation.dto.response;

import java.util.List;
import subway.application.dto.PathDto;
import subway.application.dto.ShortestPathInfoDto;

public class ShortestPathInfoResponse {

    private final List<PathDto> paths;
    private final int totalDistance;
    private final int fare;

    private ShortestPathInfoResponse(final List<PathDto> paths, final int totalDistance, final int fare) {
        this.paths = paths;
        this.totalDistance = totalDistance;
        this.fare = fare;
    }

    public static ShortestPathInfoResponse from(final ShortestPathInfoDto shortestPathInfoDto) {
        return new ShortestPathInfoResponse(
                shortestPathInfoDto.getPathDtos(),
                shortestPathInfoDto.getTotalDistance(),
                shortestPathInfoDto.getFare()
        );
    }

    public List<PathDto> getPaths() {
        return paths;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getFare() {
        return fare;
    }
}
