package subway.application.response;

public class SectionResponse {
    private final Long id;
    private final Integer distance;
    private final StationResponse upStation;
    private final StationResponse downStation;

    public SectionResponse(final Long id, final Integer distance, final StationResponse upStation, final StationResponse downStation) {
        this.id = id;
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static SectionResponse from(final Long sectionId, final int distance, final StationResponse upStation, final StationResponse downStation) {
        return new SectionResponse(sectionId, distance, upStation, downStation);
    }

    public Long getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }
}
