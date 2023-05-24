package subway.dto.service;

import static subway.domain.Line.UP_END_EDGE_DISTANCE;

import java.util.List;
import subway.domain.Line;
import subway.domain.StationEdge;
import subway.domain.StationEdges;

public class CreateLineServiceCommand {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    public CreateLineServiceCommand(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line toLine() {
        final StationEdge upEndEdge = new StationEdge(upStationId, UP_END_EDGE_DISTANCE);
        final StationEdge downEndEdge = new StationEdge(downStationId, distance);
        StationEdges stationEdges = StationEdges.from(List.of(upEndEdge, downEndEdge));
        return new Line(name, color, stationEdges);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
