package subway.entity;

public class StationEntity {

    private final Long id;
    private final Long lineId;
    private final String name;

    public StationEntity(Long id, Long lineId, String name) {
        this.id = id;
        this.lineId = lineId;
        this.name = name;
    }

    public StationEntity(Long lineId, String name) {
        this(null, lineId, name);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StationEntity{" +
                "id=" + id +
                ", lineId=" + lineId +
                ", name='" + name + '\'' +
                '}';
    }

}
