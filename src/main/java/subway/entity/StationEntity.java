package subway.entity;

public class StationEntity {
    private Long id;
    private String name;
    private Long lineId;

    public StationEntity(String name, Long lineId) {
        this(null, name, lineId);
    }

    public StationEntity(final Long id, final String name, final Long lineId) {
        this.id = id;
        this.name = name;
        this.lineId = lineId;
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
