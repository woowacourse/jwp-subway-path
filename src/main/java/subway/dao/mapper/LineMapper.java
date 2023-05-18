package subway.dao.mapper;

import java.util.List;
import java.util.stream.Collectors;
import subway.dao.dto.LineWithSection;
import subway.domain.line.LineWithSectionRes;

public final class LineMapper {

    public static List<LineWithSectionRes> convertLineWithSectionRes(final List<LineWithSection> lineWithSections) {
        return lineWithSections.stream()
            .map(LineMapper::convertLineWithSectionRes)
            .collect(Collectors.toUnmodifiableList());
    }

    private static LineWithSectionRes convertLineWithSectionRes(final LineWithSection section) {
        return new LineWithSectionRes(section.getLineId(), section.getLineName(), section.getLineColor(),
            section.getLineExtraFare(), section.getSourceStationId(), section.getSourceStationName(),
            section.getTargetStationId(), section.getTargetStationName(), section.getDistance());
    }
}
