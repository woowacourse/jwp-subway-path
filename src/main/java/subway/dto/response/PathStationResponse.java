package subway.dto.response;

public class PathStationResponse {
    private final Long id;
    private final String name;

    public PathStationResponse(Long id, String name) {
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
