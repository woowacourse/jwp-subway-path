package subway.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.vo.Distance;
import subway.exception.BusinessException;
import subway.persistence.entity.LineSectionStationJoinDto;

public class LineConverter {


    public Line convertToLine(final List<LineSectionStationJoinDto> joinDtos) {
        final List<Line> lines = removeDuplicate(joinDtos);
        if (lines.size() == 1) {
            return lines.get(0);
        }
        throw new BusinessException("매핑 실패");
    }

    public List<Line> convertToLines(final List<LineSectionStationJoinDto> joinDtos) {
        return removeDuplicate(joinDtos);
    }

    private Line makeLine(final List<LineSectionStationJoinDto> sameLineKeyDtos) {
        final Set<Section> sectionSet = new HashSet<>();
        for (final LineSectionStationJoinDto sameLineKeyDto : sameLineKeyDtos) {
            final Station startStation = new Station(sameLineKeyDto.getStartStationId(),
                sameLineKeyDto.getStartStationName());
            final Station endStation = new Station(sameLineKeyDto.getEndStationId(),
                sameLineKeyDto.getEndStationName());
            final Section section = new Section(sameLineKeyDto.getSectionId(), startStation, endStation,
                new Distance(sameLineKeyDto.getSectionDistance()));
            sectionSet.add(section);
        }
        final Sections sections = new Sections(new ArrayList<>(sectionSet));
        final LineSectionStationJoinDto firstDto = sameLineKeyDtos.get(0);
        return new Line(firstDto.getLineId(), firstDto.getLineName(), firstDto.getLineColor(), 0L, sections);
    }

    private Map<Long, List<LineSectionStationJoinDto>> collectDtosByLineId(
        final List<LineSectionStationJoinDto> dtos) {
        final Map<Long, List<LineSectionStationJoinDto>> lineBoard = new HashMap<>();
        for (final LineSectionStationJoinDto dto : dtos) {
            if (lineBoard.containsKey(dto.getLineId())) {
                lineBoard.get(dto.getLineId()).add(dto);
                continue;
            }
            lineBoard.put(dto.getLineId(), new ArrayList<>(List.of(dto)));
        }
        return lineBoard;
    }

    private List<Line> removeDuplicate(final List<LineSectionStationJoinDto> dtos) {
        final Map<Long, List<LineSectionStationJoinDto>> lineBoard = collectDtosByLineId(dtos);
        final List<Line> lines = new ArrayList<>();
        for (final Long id : lineBoard.keySet()) {
            final List<LineSectionStationJoinDto> sameLineKeyDtos = lineBoard.get(id);
            final Line line = makeLine(sameLineKeyDtos);
            lines.add(line);
        }
        return lines;
    }

}
