package subway.dto.response;

import subway.Entity.StationEntity;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(StationEntity stationEntity) {
        return new StationResponse(stationEntity.getId(), stationEntity.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
