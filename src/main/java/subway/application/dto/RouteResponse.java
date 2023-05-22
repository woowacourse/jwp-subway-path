package subway.application.dto;

import subway.domain.Fare;
import subway.domain.Route;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class RouteResponse {
    private final List<String> shortCutRoute;
    private final Fare fare;

    public RouteResponse(final List<String> shortCutRoute, final Fare fare) {
        this.shortCutRoute = shortCutRoute;
        this.fare = fare;
    }

    public static RouteResponse of(final Route route, final Fare fare) {
        final List<String> StationNames = route.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        return new RouteResponse(StationNames, fare);
    }

    public List<String> getShortCutRoute() {
        return shortCutRoute;
    }

    public Long getFare() {
        return fare.getFare();
    }
}
