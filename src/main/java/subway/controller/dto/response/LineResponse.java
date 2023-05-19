package subway.controller.dto.response;

import subway.domain.LineSection;
import subway.domain.LineSections;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private static final int FIRST_INDEX = 0;

    private final long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(final long id, final String name,
                        final String color, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse createByDomain(final LineSections lineSections) {
        return new LineResponse(
                lineSections.getLine().getId(),
                lineSections.getLine().getName(),
                lineSections.getLine().getColor(),
                createStationResponses(lineSections.getSections()));
    }

    private static List<StationResponse> createStationResponses(final List<LineSection> sections) {
        final StationResponse firstStation = StationResponse.createByDomain(sections.get(FIRST_INDEX).getPreviousStation());
        final List<StationResponse> stationResponses = new ArrayList<>();
        stationResponses.add(firstStation);
        for (LineSection section : sections) {
            final StationResponse stationResponse = StationResponse.createByDomain(section.getNextStation());
            stationResponses.add(stationResponse);
        }
        return stationResponses;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

}
