package subway.application.response;

public class SectionResponse {
    private Long id;
    private Integer distance;
    private StationResponse upStation;
    private StationResponse downStation;

    public SectionResponse() {
    }

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
