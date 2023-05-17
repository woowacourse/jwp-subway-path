package subway.dto;

import java.util.List;

public class PathResponse {
    private final List<SectionResponse> sections;
    private final int wholeDistance;
    private final int fare;

    public PathResponse(final List<SectionResponse> sections, final int wholeDistance, final int fare) {
        this.sections = sections;
        this.wholeDistance = wholeDistance;
        this.fare = fare;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }

    public int getWholeDistance() {
        return wholeDistance;
    }

    public int getFare() {
        return fare;
    }
}
