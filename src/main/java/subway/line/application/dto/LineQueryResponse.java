package subway.line.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.line.domain.Line;
import subway.line.domain.Section;

public class LineQueryResponse {

    private final String lineName;
    private final int surcharge;
    private final List<SectionQueryResponse> stationQueryResponseList;

    public LineQueryResponse(final String lineName,
                             final int surcharge,
                             final List<SectionQueryResponse> stationQueryResponseList) {
        this.lineName = lineName;
        this.surcharge = surcharge;
        this.stationQueryResponseList = stationQueryResponseList;
    }

    public static LineQueryResponse from(final Line line) {
        final List<SectionQueryResponse> sectionQueryResponses = line.sections()
                .stream()
                .map(SectionQueryResponse::from)
                .collect(Collectors.toList());
        return new LineQueryResponse(line.name(), line.surcharge(), sectionQueryResponses);
    }

    public String getLineName() {
        return lineName;
    }

    public int getSurcharge() {
        return surcharge;
    }

    public List<SectionQueryResponse> getStationQueryResponseList() {
        return stationQueryResponseList;
    }

    public static class SectionQueryResponse {

        private final String upStationName;
        private final String downStationName;
        private final int distance;

        public SectionQueryResponse(final String upStationName, final String downStationName, final int distance) {
            this.upStationName = upStationName;
            this.downStationName = downStationName;
            this.distance = distance;
        }

        public static SectionQueryResponse from(final Section section) {
            return new SectionQueryResponse(
                    section.up().name(),
                    section.down().name(),
                    section.distance());
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
