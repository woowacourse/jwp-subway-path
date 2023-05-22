package subway.persistence.entity;

import subway.domain.Line;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final Long charge;

    public LineEntity(final Long id, final String name, final String color, final Long charge) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.charge = charge;
    }

    public static LineEntity from(final Line line) {
        return new LineEntity(line.getId(), line.getName(), line.getColor(), line.getCharge());
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

    public Long getCharge() {
        return charge;
    }
}
