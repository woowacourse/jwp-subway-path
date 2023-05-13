package subway.business.converter;

import subway.business.domain.Station;
import subway.business.domain.Stations;
import subway.presentation.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class StationDomainResponseConverter {

    public static StationResponse toResponse(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static List<StationResponse> toResponses(final Stations stations) {
        return stations.getStations().stream()
                .map(StationDomainResponseConverter::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }
}
