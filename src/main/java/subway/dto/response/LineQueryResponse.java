package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;

public class LineQueryResponse {

    private String lineName;
    private List<SectionQueryResponse> stationQueryResponseList;

    private LineQueryResponse() {
    }

    public LineQueryResponse(final String lineName, final List<SectionQueryResponse> stationQueryResponseList) {
        this.lineName = lineName;
        this.stationQueryResponseList = stationQueryResponseList;
    }

    public String getLineName() {
        return lineName;
    }

    public List<SectionQueryResponse> getStationQueryResponseList() {
        return stationQueryResponseList;
    }

    public static LineQueryResponse from(final Line line) {
        final List<SectionQueryResponse> sectionQueryResponses = line.getSections()
                .stream()
                .map(SectionQueryResponse::from)
                .collect(Collectors.toList());
        return new LineQueryResponse(line.getName(), sectionQueryResponses);
    }
}
