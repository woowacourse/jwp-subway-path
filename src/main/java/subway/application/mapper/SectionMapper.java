package subway.application.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.domain.line.LineWithSectionRes;
import subway.domain.section.Section;
import subway.domain.section.SubwayLine;
import subway.domain.station.Station;

public final class SectionMapper {

    public static SubwayLine createSubwayLine(final List<LineWithSectionRes> lineWithSections) {
        final List<Section> sections = lineWithSections.stream().map(res -> {
            if (res.getSourceStationName() == null || res.getTargetStationName() == null) {
                return Section.empty();
            }
            final Station sourceStation = Station.create(res.getSourceStationName());
            final Station targetStation = Station.create(res.getTargetStationName());
            return new Section(sourceStation, targetStation, res.getDistance());
        }).collect(Collectors.toList());
        return new SubwayLine(sections);
    }

    public static List<SubwayLine> createSubwayLines(final Map<Long, List<LineWithSectionRes>> sectionsByLineId) {
        return sectionsByLineId.values().stream()
            .map(SectionMapper::createSubwayLine)
            .collect(Collectors.toUnmodifiableList());
    }

    public static Map<Long, List<LineWithSectionRes>> getSectionsByLineId(
        final List<LineWithSectionRes> allWithSections) {
        return allWithSections.stream()
            .collect(Collectors.groupingBy(LineWithSectionRes::getLineId));
    }

    public static List<Section> getAllSections(final Map<Long, List<LineWithSectionRes>> sectionsByLineId) {
        return sectionsByLineId.values().stream()
            .map(SectionMapper::createSubwayLine)
            .flatMap(sections -> sections.getSections().stream())
            .collect(Collectors.toList());
    }
}
