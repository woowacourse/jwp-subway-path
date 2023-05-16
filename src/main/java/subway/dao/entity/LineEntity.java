package subway.dao.entity;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public LineEntity(final String name, final String color, final int extraFare) {
        this(null, name, color, extraFare);
    }

    public LineEntity(final Long id, final String name, final String color, final int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public int getExtraFare() {
        return extraFare;
    }
}
