package subway.dto.response;

public class SectionResponse {

    private Long id;
    private StationResponse upStationResponse;
    private StationResponse downStationResponse;
    private int distance;

    public SectionResponse() {
    }

    public SectionResponse(
            final Long id,
            final StationResponse upStationResponse,
            final StationResponse downStationResponse,
            final int distance
    ) {
        this.id = id;
        this.upStationResponse = upStationResponse;
        this.downStationResponse = downStationResponse;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getUpStationResponse() {
        return upStationResponse;
    }

    public StationResponse getDownStationResponse() {
        return downStationResponse;
    }

    public int getDistance() {
        return distance;
    }
}
