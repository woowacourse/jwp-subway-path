package subway.ui.dto.response;

import subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private long id;
    private String name;

    public LineResponse() {
    }

    public LineResponse(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static List<LineResponse> of(final List<Line> lines) {
        return lines.stream()
                .map(line -> new LineResponse(line.getId(), line.getName()))
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class SectionDeleteRequest {
        private String upStationName;
        private String downStationName;

        public SectionDeleteRequest() {
        }

        public SectionDeleteRequest(final String upStationName, final String downStationName) {
            this.upStationName = upStationName;
            this.downStationName = downStationName;
        }

        public String getUpStationName() {
            return upStationName;
        }

        public String getDownStationName() {
            return downStationName;
        }
    }
}
