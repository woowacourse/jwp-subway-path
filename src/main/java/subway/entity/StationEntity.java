package subway.entity;

public class StationEntity {

    private final Long id;
    private final String name;
    private final Long lineId;

    public StationEntity(final Long id, final String name, final Long lineId) {
        this.id = id;
        this.name = name;
        this.lineId = lineId;
    }

    public StationEntity(final String name, final Long lineId) {
        this(null, name, lineId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getLineId() {
        return lineId;
    }
}
