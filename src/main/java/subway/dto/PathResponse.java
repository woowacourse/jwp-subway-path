package subway.dto;

public class PathResponse {
    private final Integer distance;
    private final Integer price;

    public PathResponse(Integer distance, Integer price) {
        this.distance = distance;
        this.price = price;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getPrice() {
        return price;
    }
}
