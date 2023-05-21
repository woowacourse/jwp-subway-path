package subway.ui.dto.path;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import subway.application.dto.path.ShortestPathDto;
import subway.ui.dto.station.StationResponse;

public class ShortestPathResponse {
    private List<StationResponse> pathStations;
    private double distance;
    private BigDecimal fare;

    public ShortestPathResponse(List<StationResponse> pathStations, double distance, BigDecimal fare) {
        this.pathStations = pathStations;
        this.distance = distance;
        this.fare = fare;
    }

    public static ShortestPathResponse from(ShortestPathDto pathDto) {
        List<StationResponse> pathStations = pathDto.getStations()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new ShortestPathResponse(pathStations, pathDto.getDistance(), pathDto.getFare());
    }

    public List<StationResponse> getPathStations() {
        return pathStations;
    }

    public double getDistance() {
        return distance;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
