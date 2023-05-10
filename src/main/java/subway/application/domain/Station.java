package subway.application.domain;

public class Station {

    private final long id;
    private final String name;

    public Station(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
