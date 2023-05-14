package subway.application.port.in.line.dto.response;

import java.util.Collections;
import java.util.List;
import subway.adapter.out.persistence.entity.LineEntity;
import subway.application.port.in.station.dto.response.StationQueryResponse;

public class LineQueryResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationQueryResponse> stations;

    public LineQueryResponse() {
    }

    public LineQueryResponse(final Long id, final String name, final String color,
            final List<StationQueryResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineQueryResponse of(LineEntity lineEntity) {
        return new LineQueryResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
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

    public List<StationQueryResponse> getStations() {
        return stations;
    }
}
