package subway.entity;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final Long upBoundStationId;
    private final Long downBoundStationId;

    public LineEntity(final Long id, final String name, final String color, final Long upBoundStationId, final Long downBoundStationId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upBoundStationId = upBoundStationId;
        this.downBoundStationId = downBoundStationId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpBoundStationId() {
        return upBoundStationId;
    }

    public Long getDownBoundStationId() {
        return downBoundStationId;
    }
}
