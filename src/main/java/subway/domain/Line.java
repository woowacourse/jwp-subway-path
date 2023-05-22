package subway.domain;

public class Line {
    private final Long id;
    private final String name;
    private final int surcharge;

    public Line(final String name, final int surcharge) {
        this(null, name, surcharge);
    }

    public Line(final Long id, final String name, final int surcharge) {
        this.id = id;
        this.name = name;
        this.surcharge = surcharge;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSurcharge() {
        return surcharge;
    }
}
