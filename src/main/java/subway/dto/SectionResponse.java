package subway.dto;

import subway.domain.section.Section;

public class SectionResponse {

    private StationResponse from;
    private StationResponse to;
    private Integer distance;

    public SectionResponse() {
    }

    public SectionResponse(final StationResponse from, final StationResponse to, final Integer distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public static SectionResponse of(final Section section) {
        return new SectionResponse(StationResponse.of(section.getFrom()), StationResponse.of(section.getTo()), section.getDistanceValue());
    }

    public StationResponse getFrom() {
        return from;
    }

    public StationResponse getTo() {
        return to;
    }

    public Integer getDistance() {
        return distance;
    }
}
