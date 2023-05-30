package subway.entity;

import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.StationResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StationEntity {

    private final Long id;
    private final String name;
    private final Long nextStationId;
    private final Integer distance;
    private final Long lineId;

    public StationEntity(Long id, String name, Long nextStationId, Integer distance, Long lineId) {
        this.id = id;
        this.name = name;
        this.nextStationId = nextStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public StationEntity(String name) {
        this(null, name, null, null, null);
    }

    public static Stations convertToStations(List<StationEntity> stationEntity) {
        return new Stations(stationEntity.stream()
                .map(entity -> new Station(entity.getName(), new Distance(entity.getDistance())))
                .collect(Collectors.toList()));
    }

    public StationResponse convertToResponse() {
        return new StationResponse(id, name);
    }

    public Station convertToStation() {
        return new Station(name, new Distance(distance));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationEntity that = (StationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(nextStationId, that.nextStationId) && Objects.equals(distance, that.distance)
                && Objects.equals(lineId, that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nextStationId, distance, lineId);
    }
}
