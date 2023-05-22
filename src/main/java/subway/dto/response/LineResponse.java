package subway.dto.response;

import subway.entity.LineEntity;

import java.util.Collections;
import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int cost;
    private List<StationResponse> stations;

    public LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final int cost, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.stations = stations;
    }

    public static LineResponse of(LineEntity lineEntity) {
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), lineEntity.getCost(),
                Collections.emptyList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getCost() {
        return cost;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
