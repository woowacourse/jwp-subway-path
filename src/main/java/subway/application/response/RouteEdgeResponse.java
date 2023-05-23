package subway.application.response;

import subway.domain.route.RouteEdge;

public class RouteEdgeResponse {

    private String startStation;
    private String endStation;
    private String lineName;
    private Integer distance;

    public RouteEdgeResponse() {
    }

    public RouteEdgeResponse(final String startStation, final String endStation, final String lineName, final Integer distance) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.lineName = lineName;
        this.distance = distance;
    }

    public static RouteEdgeResponse from(final RouteEdge routeEdge) {
        return new RouteEdgeResponse(
                routeEdge.getUpStation().getNameValue(),
                routeEdge.getDownStation().getNameValue(),
                routeEdge.getLine().getNameValue(),
                routeEdge.getDistance().getValue()
        );
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public String getLineName() {
        return lineName;
    }

    public Integer getDistance() {
        return distance;
    }
}
