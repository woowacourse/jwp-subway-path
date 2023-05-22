package subway.application.mapper;

import static subway.application.mapper.SectionMapper.createSubwayLine;
import static subway.application.mapper.StationMapper.createStationResponses;

import java.util.List;
import subway.application.dto.LineRequest;
import subway.application.dto.LineResponse;
import subway.application.dto.StationResponse;
import subway.domain.line.Line;
import subway.domain.line.LineWithSectionRes;
import subway.domain.section.SubwayLine;
import subway.domain.station.Station;

public class LineMapper {

    public static Line createLine(final List<LineWithSectionRes> lineWithSections) {
        final SubwayLine subwayLine = createSubwayLine(lineWithSections);
        return new Line(lineWithSections.get(0).getLineName(),
            lineWithSections.get(0).getLineColor(), lineWithSections.get(0).getExtraFare(), subwayLine);
    }

    public static Line createLine(final LineRequest request) {
        return new Line(request.getName(), request.getColor(), request.getExtraFare());
    }

    public static LineResponse createLineResponse(final List<LineWithSectionRes> lineWithSections, final Line line) {
        final SubwayLine subwayLine = line.subwayLine();
        final List<Station> sortedStations = subwayLine.getSortedStations();
        final List<StationResponse> stationResponses = createStationResponses(lineWithSections, sortedStations);
        return new LineResponse(lineWithSections.get(0).getLineId(), line.name().name(), line.color(),
            line.extraFare().fare(), stationResponses);
    }
}
