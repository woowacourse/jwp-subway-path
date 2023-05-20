package subway.section.presentation.dto.response;

import subway.section.domain.Section;
import subway.station.presentation.dto.response.StationResponse;

public class SectionResponse {

    private final Long id;
    private final StationResponse upStation;
    private final StationResponse downStation;
    private final int distance;

    public SectionResponse(final Long id, final StationResponse upStation, final StationResponse downStation, final int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionResponse from(final Section section) {
        return new SectionResponse(
                section.getLineId(),
                StationResponse.from(section.getUpStation()),
                StationResponse.from(section.getDownStation()),
                section.getDistanceValue()
        );
    }

    public Long getId() {
        return id;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

}
