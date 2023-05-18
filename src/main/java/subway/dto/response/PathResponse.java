package subway.dto.response;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> path;
    private final double Money;

    public PathResponse(List<StationResponse> path, double money) {
        this.path = path;
        Money = money;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public double getMoney() {
        return Money;
    }
}
