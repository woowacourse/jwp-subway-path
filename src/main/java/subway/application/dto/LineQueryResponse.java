package subway.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Section;

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

    public static class SectionQueryResponse {

        private String upStationName;
        private String downStationName;
        private int distance;

        private SectionQueryResponse() {
        }

        public SectionQueryResponse(final String upStationName, final String downStationName, final int distance) {
            this.upStationName = upStationName;
            this.downStationName = downStationName;
            this.distance = distance;
        }

        public static SectionQueryResponse from(final Section section) {
            return new SectionQueryResponse(
                    section.getUp().getName(),
                    section.getDown().getName(),
                    section.getDistance());
        }

        public String getUpStationName() {
            return upStationName;
        }

        public String getDownStationName() {
            return downStationName;
        }

        public int getDistance() {
            return distance;
        }
    }
}
