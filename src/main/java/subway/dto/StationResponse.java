package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.entity.StationEntity;

@Getter
@AllArgsConstructor
public class StationResponse {
    private Long id;
    private String name;

    public static StationResponse of(StationEntity station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
