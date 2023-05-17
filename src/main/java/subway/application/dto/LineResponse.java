package subway.application.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import subway.domain.Line;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stationResponses;

    public LineResponse(final Long id, final String name, final String color, final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineResponse of(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), new ArrayList<>());
    }

    public static LineResponse of(final Line line, final List<StationResponse> stationResponses) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
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

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineResponse that = (LineResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(stationResponses, that.stationResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stationResponses);
    }

    @Override
    public String toString() {
        return "LineResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", stationResponses=" + stationResponses +
                '}';
    }
}
