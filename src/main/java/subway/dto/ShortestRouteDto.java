package subway.dto;

import java.util.List;
import subway.domain.Station;

public class ShortestRouteDto {
    private final Double totalDistance;
    private final List<Station> stationsInRoute;

    public ShortestRouteDto(Double totalDistance,
                            List<Station> stationsInRoute) {
        this.totalDistance = totalDistance;
        this.stationsInRoute = stationsInRoute;
    }

}
