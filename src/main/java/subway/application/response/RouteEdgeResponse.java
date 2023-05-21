package subway.application.response;

import subway.domain.route.RouteEdge;

public class RouteEdgeResponse {

    private final String startStationName;
    private final String endStationName;
    private final String lineName;
    private final Integer distance;

    public RouteEdgeResponse(final String startStationName, final String endStationName, final String lineName, final Integer distance) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.lineName = lineName;
        this.distance = distance;
    }

    public static RouteEdgeResponse from(final RouteEdge routeEdge) {
        return new RouteEdgeResponse(
                routeEdge.getUpStation().getNameValue(),
                routeEdge.getDownStation().getNameValue(),
                routeEdge.getLineName().getValue(),
                routeEdge.getDistance().getValue()
        );
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public String getLineName() {
        return lineName;
    }

    public Integer getDistance() {
        return distance;
    }
}
