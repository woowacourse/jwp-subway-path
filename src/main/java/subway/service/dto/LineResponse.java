package subway.service.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;

public class LineResponse {

    private final Long id;
    private final String name;
    private final List<StationResponse> stations;

    public LineResponse(final Long id, final String name, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    private LineResponse() {
        this(null, null, null);
    }

    public static LineResponse from(final Line line) {
        final List<StationResponse> stationResponses = line.getSections().getAllStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new LineResponse(line.getId(), line.getName().getValue(), stationResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
