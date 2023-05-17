package subway.entity;

public class LineEntity {
    private Long id;
    private String name;
    private String color;
    private Integer surcharge;

    public LineEntity(final String name, final String color, final Integer surcharge) {
        this(null, name, color, surcharge);
    }

    public LineEntity(final Long id, final String name, final String color, final Integer surcharge) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
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

    public Integer getSurcharge() {
        return surcharge;
    }
}
