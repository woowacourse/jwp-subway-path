package subway.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import java.util.stream.Collectors;
import subway.application.dto.ReadLineDto;

@JsonInclude(Include.NON_EMPTY)
public class ReadLineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<ReadStationResponse> stationResponses;

    public ReadLineResponse(
            final Long id,
            final String name,
            final String color,
            final List<ReadStationResponse> stationResponses
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static ReadLineResponse from(final ReadLineDto dto) {
        final List<ReadStationResponse> stationResponses = dto.getStationDtos().stream()
                .map(ReadStationResponse::from)
                .collect(Collectors.toList());

        return new ReadLineResponse(dto.getId(), dto.getName(), dto.getColor(), stationResponses);
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

    public List<ReadStationResponse> getStationResponses() {
        return stationResponses;
    }
}
