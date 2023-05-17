package subway.dto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

public class SectionMapper {
    private final Map<Long, Station> idToStation;
    private final Map<Long, Line> idToLine;

    private SectionMapper(Map<Long, Station> idToStation, Map<Long, Line> idToLine) {
        this.idToStation = idToStation;
        this.idToLine = idToLine;
    }

    public static SectionMapper from(List<Station> stations, List<Line> lines) {
        final Map<Long, Station> idToStation = stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
        final Map<Long, Line> idToLine = lines.stream()
                .collect(Collectors.toMap(Line::getId, Function.identity()));
        return new SectionMapper(idToStation, idToLine);
    }

    public List<SectionResponse> convertToSectionResponse(List<Section> sections) {
        return sections.stream()
                .map(section -> SectionResponse.from(
                                idToLine.get(section.getLineId()),
                                idToStation.get(section.getSourceStationId()),
                                idToStation.get(section.getTargetStationId())
                        )
                )
                .collect(Collectors.toUnmodifiableList());
    }
}
