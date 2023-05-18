package subway.business.converter.line;

import subway.business.domain.LineSection;
import subway.business.domain.LineSections;
import subway.business.domain.Station;
import subway.presentation.dto.response.LineResponse;
import subway.presentation.dto.response.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineSectionsResponseConverter {

    private static final int FIRST_INDEX = 0;

    public static LineResponse toResponse(final LineSections lineSections) {
        return new LineResponse(
                lineSections.getLine().getId(),
                lineSections.getLine().getName(),
                lineSections.getLine().getColor(),
                toStationResponses(lineSections.getSections()));
    }

    private static List<StationResponse> toStationResponses(final List<LineSection> sections) {
        final StationResponse firstStation = toStationResponse(sections.get(FIRST_INDEX).getPreviousStation());
        final List<StationResponse> stationResponses = new ArrayList<>();
        stationResponses.add(firstStation);
        for (LineSection section : sections) {
            final StationResponse stationResponse = toStationResponse(section.getNextStation());
            stationResponses.add(stationResponse);
        }
        return stationResponses;
    }

    private static StationResponse toStationResponse(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
