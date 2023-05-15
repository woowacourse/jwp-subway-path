package subway.domain.station.dto;

public class StationRes {

    private final Long id;
    private final String name;

    public StationRes(final Long id, final String name) {
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
