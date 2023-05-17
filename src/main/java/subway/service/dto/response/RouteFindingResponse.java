package subway.service.dto.response;

import java.util.List;

public class RouteFindingResponse {

    private final List<String> route;
    private final double distance;

    private final int price;


    public RouteFindingResponse(List<String> route, double distance, int price) {
        this.route = route;
        this.distance = distance;
        this.price = price;
    }

    public List<String> getRoute() {
        return route;
    }

    public double getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }

}
