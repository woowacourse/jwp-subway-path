package subway.application.dto;

import java.util.List;
import subway.domain.fare.FareAmount;

public class ShortestPathInfoDto {

    private final List<PathDto> pathDtos;
    private final int totalDistance;
    private final int fare;

    private ShortestPathInfoDto(final List<PathDto> pathDtos, final int totalDistance, final int fare) {
        this.pathDtos = pathDtos;
        this.totalDistance = totalDistance;
        this.fare = fare;
    }

    public static ShortestPathInfoDto of(final ShortestPathsDto shortestPathsDto, final FareAmount fareAmount) {
        return new ShortestPathInfoDto(
                shortestPathsDto.getPathDtos(),
                shortestPathsDto.getTotalDistance(),
                fareAmount.getAmount()
        );
    }

    public List<PathDto> getPathDtos() {
        return pathDtos;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getFare() {
        return fare;
    }
}
