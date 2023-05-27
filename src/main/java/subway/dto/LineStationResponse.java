package subway.dto;

import java.util.List;

public class LineStationResponse {

    private final LineResponse lineResponse;
    private final List<StationResponse> stationResponses;
    private final List<SectionResponse> sectionResponses;

    private LineStationResponse(final LineResponse lineResponse, final List<StationResponse> stationResponses,
                                final List<SectionResponse> sectionResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
        this.sectionResponses = sectionResponses;
    }

    public static LineStationResponse from(final LineResponse lineResponse, final List<StationResponse> stationResponses,
                                           final List<SectionResponse> sectionResponses) {
        return new LineStationResponse(lineResponse, stationResponses, sectionResponses);
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public List<SectionResponse> getSectionResponses() {
        return sectionResponses;
    }
}
