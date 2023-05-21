package subway.domain.line.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import subway.domain.line.domain.Line;
import subway.domain.line.entity.LineEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    @JsonProperty("stations")
    private List<StationResponse> stationResponses;

    private LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineResponse of(final Line line) {
        List<StationResponse> stations = line.getStations().
                stream().
                map(StationResponse::of).
                collect(Collectors.toList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    public static LineResponse of(final LineEntity lineEntity) {
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), null);
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineResponse that = (LineResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
