package subway.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import subway.domain.Line;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(Include.NON_EMPTY)
public class ReadLineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<ReadStationResponse> stations;

    public ReadLineResponse(final Long id,
                            final String name,
                            final String color,
                            final List<ReadStationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static ReadLineResponse of(final Line line) {
        final List<Station> stations = line.findStationsByOrdered();
        final List<ReadStationResponse> stationResponses = stations.stream()
                .map(ReadStationResponse::from)
                .collect(Collectors.toList());

        return new ReadLineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
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

    public List<ReadStationResponse> getStations() {
        return stations;
    }
}
