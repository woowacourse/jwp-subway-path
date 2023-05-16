package subway.dto;

import java.util.List;

public class PathResponse {
    private final Integer distance;
    private final Integer price;
    private final List<String> path;

    public PathResponse(Integer distance, Integer price, List<String> path) {
        this.distance = distance;
        this.price = price;
        this.path = path;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getPrice() {
        return price;
    }
}
