package subway.application.dto;

import java.util.List;

public class PathDto {
    private final List<StationDto> path;
    private final Integer cost;

    public PathDto(List<StationDto> path, Integer cost) {
        this.path = path;
        this.cost = cost;
    }

    public List<StationDto> getPath() {
        return path;
    }

    public Integer getCost() {
        return cost;
    }
}
