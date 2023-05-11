package subway.dto;

import subway.domain.Line;
import subway.domain.Section;

import java.util.ArrayList;
import java.util.List;

public class FinalLineResponse {

    private final Long id;
    private final String name;
    private final List<StationResponse> stations;

    public FinalLineResponse(final Long id, final String name, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    public static FinalLineResponse from(final Line line) {
        if (line.getSections().getSections().isEmpty()) {
            return new FinalLineResponse(line.getId(), line.getName().getValue(), new ArrayList<>());
        }
        final List<StationResponse> stationResponses = new ArrayList<>();
        final List<Section> sections = line.getSections().getSections();
        for (final Section section : sections) {
            stationResponses.add(StationResponse.of(section.getBeforeStation()));
        }
        stationResponses.add(StationResponse.of(sections.get(sections.size() - 1).getNextStation()));
        return new FinalLineResponse(line.getId(), line.getName().getValue(), stationResponses);
    }

    private FinalLineResponse() {
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
