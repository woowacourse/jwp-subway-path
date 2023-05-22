package subway.adapter.out.persistence.entity;

public class LineEntity {
    private final Long id;
    private final String name;
    private final int surcharge;

    public LineEntity(final String name,final int surcharge) {
        this(null, name, surcharge);
    }

    public LineEntity(final Long id, final String name, final int surcharge) {
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
