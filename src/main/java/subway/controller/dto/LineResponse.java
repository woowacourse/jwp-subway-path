package subway.controller.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Section;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(final Line line) {
        final List<StationResponse> stations = getStationResponses(line);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private static List<StationResponse> getStationResponses(final Line line) {
        if (line.getSections() == null) {
            return Collections.emptyList();
        }
        final List<Section> sections = line.getSections().getValue();
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        final List<StationResponse> stations = sections.stream()
            .map(section -> StationResponse.of(section.getUpStation()))
            .collect(Collectors.toList());
        stations.add(StationResponse.of(sections.get(sections.size() - 1).getDownStation()));
        return stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
