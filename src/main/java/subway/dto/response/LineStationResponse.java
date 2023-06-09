package subway.dto.response;

public class LineStationResponse {
    private final Long id;
    private final String name;

    public LineStationResponse(Long id, String name) {
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
