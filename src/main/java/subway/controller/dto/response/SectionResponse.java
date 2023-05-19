package subway.controller.dto.response;

import subway.service.domain.Section;

public class SectionResponse {

    private final Long id;
    private final Integer distance;
    private final StationResponse previousStation;
    private final StationResponse nextStation;

    private SectionResponse(Long id,
                            Integer distance,
                            StationResponse previousStation,
                            StationResponse nextStation) {
        this.id = id;
        this.distance = distance;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getDistance(),
                StationResponse.from(section.getPreviousStation()),
                StationResponse.from(section.getNextStation())
        );
    }

    public Long getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public StationResponse getPreviousStation() {
        return previousStation;
    }

    public StationResponse getNextStation() {
        return nextStation;
    }

}
