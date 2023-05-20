package subway.application.response;

import subway.domain.vo.Distance;
import subway.domain.vo.Money;
import subway.domain.route.Route;

import java.util.List;
import java.util.stream.Collectors;

public class QueryShortestRouteResponse {

    private String startStation;
    private String endStation;
    private List<String> transferStations;
    private List<RouteEdgeResponse> sections;
    private Integer totalDistance;
    private String totalPrice;

    public QueryShortestRouteResponse() {
    }

    public QueryShortestRouteResponse(
            final String startStation,
            final String endStation,
            final List<String> transferStations,
            final List<RouteEdgeResponse> sections,
            final Integer totalDistance,
            final String totalPrice
    ) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.transferStations = transferStations;
        this.sections = sections;
        this.totalDistance = totalDistance;
        this.totalPrice = totalPrice;
    }

    public static QueryShortestRouteResponse from(
            final StationResponse startStation,
            final StationResponse endStation,
            final List<StationResponse> transferStations,
            final List<RouteEdgeResponse> sectionEdges,
            final Distance distance,
            final Money totalPrice
    ) {

        return new QueryShortestRouteResponse(
                startStation.getName(),
                endStation.getName(),
                transferStations.stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()),
                sectionEdges,
                distance.getValue(),
                totalPrice.getValue()
        );
    }

    public static QueryShortestRouteResponse from(final Route route) {
        final StationResponse startStationResponse = StationResponse.from(route.getStart());
        final StationResponse endStationResponse = StationResponse.from(route.getEnd());
        final List<String> transferStationResponse = collectTransferStationResponses(route);
        final List<RouteEdgeResponse> routeEdgeResponse = collectRouteEdgeResponses(route);
        final Distance distance = route.getTotalDistance();
        final Money totalPrice = route.getTotalPrice();

        return new QueryShortestRouteResponse(
                startStationResponse.getName(),
                endStationResponse.getName(),
                transferStationResponse,
                routeEdgeResponse,
                distance.getValue(),
                totalPrice.getValue()
        );
    }

    private static List<RouteEdgeResponse> collectRouteEdgeResponses(final Route route) {
        return route.getSections()
                .stream()
                .map(RouteEdgeResponse::from)
                .collect(Collectors.toList());
    }

    private static List<String> collectTransferStationResponses(final Route route) {
        return route.getTransfers()
                .stream()
                .map(StationResponse::from)
                .map(StationResponse::getName)
                .collect(Collectors.toList());
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public List<String> getTransferStations() {
        return transferStations;
    }

    public List<RouteEdgeResponse> getSections() {
        return sections;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
}
