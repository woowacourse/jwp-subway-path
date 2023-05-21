package subway.entity;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final Long headStationId;

    public LineEntity(Long id, String name, String color, Long headStationId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.headStationId = headStationId;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public Long getHeadStationId() {
        return headStationId;
    }
}
