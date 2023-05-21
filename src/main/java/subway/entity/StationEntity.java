package subway.entity;

import static subway.application.StationService.EMPTY_STATION_ID;

import java.util.Objects;

public class StationEntity {

    private final Long id;
    private final String name;
    private final Long next;
    private final Integer distance;
    private final Long lineId;

    public StationEntity(Long id, String name, Long next, Integer distance, Long lineId) {
        this.id = id;
        this.name = name;
        this.next = next;
        this.distance = distance;
        this.lineId = lineId;
    }

    public StationEntity(String name, Long next, Integer distance, Long lineId) {
        this(0L, name, next, distance, lineId);
    }
    public StationEntity(String name, Long lineId) {
        this(0L, name, EMPTY_STATION_ID, null, lineId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getNext() {
        return next;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationEntity that = (StationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
            && Objects.equals(next, that.next) && Objects.equals(distance, that.distance)
            && Objects.equals(lineId, that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, next, distance, lineId);
    }
}
