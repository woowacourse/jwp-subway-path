package subway.line.persistence;

public class LineEntity {

    private final Long id;
    private final String lineName;

    public LineEntity(String lineName) {
        this(null, lineName);
    }

    public LineEntity(final Long id, final String lineName) {
        this.id = id;
        this.lineName = lineName;
    }

    public Long getId() {
        return id;
    }

    public String getLineName() {
        return lineName;
    }
}
