package subway.dto.response;

import java.util.List;

public class PathAndFareResponse {
    private final List<StationResponse> path;
    private final Long money;

    public PathAndFareResponse(List<StationResponse> path, long money) {
        this.path = path;
        this.money = money;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public Long getMoney() {
        return money;
    }
}
