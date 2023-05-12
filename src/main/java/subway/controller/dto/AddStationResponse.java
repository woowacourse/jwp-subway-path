package subway.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Station;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddStationResponse {

    private long stationId;
    private String stationName;

    public static AddStationResponse from(final Station station) {
        return new AddStationResponse(station.getId(), station.getName());
    }
}
