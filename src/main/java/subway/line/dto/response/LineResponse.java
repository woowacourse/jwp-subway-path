package subway.line.dto.response;

import java.util.List;
import subway.line.domain.line.Line;

public class LineResponse {

    private final Long id;
    private final String title;
    private final String color;
    private final List<InterStationResponse> interStations;

    public LineResponse(Long id, String title, String color, List<InterStationResponse> interStations) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.interStations = interStations;
    }

    public static LineResponse from(Line savedLine) {
        return new LineResponse(savedLine.getId(), savedLine.getName().getValue(),
                savedLine.getColor().getValue(),
                InterStationResponse.from(savedLine.getInterStations().getInterStations()));
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }

    public List<InterStationResponse> getInterStations() {
        return interStations;
    }
}
