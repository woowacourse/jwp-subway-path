package subway.service.converter.section;

import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.LineSection;
import subway.domain.LineSections;
import subway.domain.Station;
import subway.entity.SectionDetailEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SectionDetailEntityDomainConverter {

    private static final int FIRST_INDEX = 0;

    public static LineSections toLineSections(final List<SectionDetailEntity> entities) {
        final SectionDetailEntity entity = entities.get(FIRST_INDEX);
        final Line line = new Line(entity.getLineId(), entity.getLineName(), entity.getLineColor());
        final List<LineSection> sections = entities.stream()
                .map(SectionDetailEntityDomainConverter::toLineSection)
                .collect(Collectors.toUnmodifiableList());
        return LineSections.createWithSort(line, sections);
    }

    private static LineSection toLineSection(final SectionDetailEntity entity) {
        return new LineSection(
                entity.getId(),
                new Station(entity.getPreviousStationId(), entity.getPreviousStationName()),
                new Station(entity.getNextStationId(), entity.getNextStationName()),
                new Distance(entity.getDistance())
        );
    }

}
