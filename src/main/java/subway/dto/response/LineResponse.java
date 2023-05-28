package subway.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import subway.domain.subwaymap.Line;
import subway.domain.subwaymap.Section;
import subway.domain.subwaymap.Sections;
import subway.dto.LineSectionDto;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private Integer additionalFare;
    private List<StationResponse> stations;

    LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final Integer additionalFare,
        final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.stations = stations;
    }

    public static LineResponse of(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare(),
            new ArrayList<>());
    }

    public static LineResponse withStationResponses(final Line line, final List<StationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare(), stations);
    }

    public static List<LineResponse> of(final List<LineSectionDto> linesSectionsDtos) {
        final Map<Line, List<LineSectionDto>> lines = linesSectionsDtos.stream()
            .collect(Collectors.groupingBy(LineSectionDto::getLine));
        return lines.entrySet()
            .stream()
            .map(LineResponse::generateLineResponse)
            .collect(Collectors.toList());
    }

    private static LineResponse generateLineResponse(final Entry<Line, List<LineSectionDto>> entry) {
        final Line line = entry.getKey();
        final List<Section> sectionList = entry.getValue()
            .stream()
            .map(LineSectionDto::getSection)
            .collect(Collectors.toList());
        final Sections lineSections = Sections.of(sectionList);
        final List<StationResponse> stationResponses = lineSections.getSortedStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return LineResponse.withStationResponses(line, stationResponses);
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getAdditionalFare() {
        return additionalFare;
    }
}
