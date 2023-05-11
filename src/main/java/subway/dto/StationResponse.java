package subway.dto;

public class StationResponse {
    private final Long id;
    private final String name;

    public StationResponse() {
        this(null, null);
    }

    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(final Long id, final String name) {
        return new StationResponse(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
