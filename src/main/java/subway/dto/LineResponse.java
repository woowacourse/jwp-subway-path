package subway.dto;

import subway.domain.Line;
import subway.domain.Section;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private final Long id;
    private final String name;
    private final List<StationResponse> stations;

    public LineResponse(final Long id, final String name, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    public static LineResponse from(final Line line) {
        if (line.getSections().getSections().isEmpty()) {
            return new LineResponse(line.getId(), line.getName().getValue(), new ArrayList<>());
        }
        final List<StationResponse> stationResponses = new ArrayList<>();
        final List<Section> sections = line.getSections().getSections();
        for (final Section section : sections) {
            stationResponses.add(StationResponse.of(section.getBeforeStation()));
        }
        stationResponses.add(StationResponse.of(sections.get(sections.size() - 1).getNextStation()));
        return new LineResponse(line.getId(), line.getName().getValue(), stationResponses);
    }

    private LineResponse() {
        this(null, null, null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
