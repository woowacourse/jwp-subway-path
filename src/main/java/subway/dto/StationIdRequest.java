package subway.dto;

public class StationIdRequest {

    private Long id;

    private StationIdRequest() {
    }

    public StationIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
