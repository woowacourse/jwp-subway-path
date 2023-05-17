package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.persistence.entity.StationEntity;

@Getter
@AllArgsConstructor
public class StationResponse {
    private final Long id;
    private final String name;

    public static StationResponse of(StationEntity station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
