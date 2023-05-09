package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.entity.StationEntity;

@Getter
@AllArgsConstructor
public class StationResponse {

    private final Long id;
    private final String name;

    public static StationResponse of(final StationEntity stationEntity) {
        return new StationResponse(stationEntity.getId(), stationEntity.getName());
    }
}
