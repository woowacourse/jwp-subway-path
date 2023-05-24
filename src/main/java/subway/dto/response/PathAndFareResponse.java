package subway.dto.response;

import subway.domain.Station;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathAndFareResponse {
    private final List<StationResponse> path;
    private final Long money;

    public PathAndFareResponse(List<Station> path, long money) {
        this.path = path.stream().map(StationResponse::of).collect(Collectors.toList());
        this.money = money;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public Long getMoney() {
        return money;
    }
}
