package subway.infrastructure.entity;

public class StationRow {

    private final Long id;
    private final String name;

    public StationRow(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
