package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.entity.StationEntity;

public class StationResponse {
    
    private final Long id;
    private final String name;
    
    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
    
    public static StationResponse of(final StationEntity stationEntity) {
        return new StationResponse(stationEntity.getId(), stationEntity.getName());
    }
    
    public static List<StationResponse> listOf(final List<StationEntity> stationEntities) {
        return stationEntities.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
}
