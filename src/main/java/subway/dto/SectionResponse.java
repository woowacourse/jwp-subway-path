package subway.dto;

import subway.domain.Section;

public class SectionResponse {

    private final StationResponse upStation;
    private final StationResponse downStation;
    private final int distance;

    private SectionResponse(final StationResponse upStation, final StationResponse downStation, final int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionResponse of(final Section section) {
        return new SectionResponse(
                StationResponse.of(section.getUpStation()),
                StationResponse.of(section.getDownStation()),
                section.getDistance()
        );
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
