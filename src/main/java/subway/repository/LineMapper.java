package subway.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;
import subway.domain.core.Distance;
import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.domain.core.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@Component
public class LineMapper {

    public List<SectionEntity> toSectionEntities(
            final Line line,
            final Long lineId,
            final List<StationEntity> stationEntities
    ) {
        final Map<String, Long> stationNameByStationId = stationEntities.stream()
                .collect(toMap(StationEntity::getName, StationEntity::getId));

        return line.getSections().stream()
                .filter(section -> Objects.isNull(section.getId()))
                .map(section -> new SectionEntity(
                        stationNameByStationId.get(section.getStartName()),
                        stationNameByStationId.get(section.getEndName()),
                        section.getDistanceValue(),
                        lineId
                ))
                .collect(toList());
    }

    public List<StationEntity> toStationEntities(
            final Line line,
            final Long lineId
    ) {
        return line.findAllStation().stream()
                .map(station -> new StationEntity(station.getName(), lineId))
                .collect(toList());
    }

    public Line toLine(
            final LineEntity lineEntity,
            final List<SectionEntity> sectionEntities,
            final List<StationEntity> stationEntities
    ) {
        final Map<Long, String> stationIdByStationName = stationEntities.stream()
                .collect(toMap(StationEntity::getId, StationEntity::getName));
        final List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> toSection(stationIdByStationName, sectionEntity))
                .collect(toList());

        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                lineEntity.getSurcharge(),
                sections
        );
    }

    private static Section toSection(
            final Map<Long, String> stationIdByStationName,
            final SectionEntity sectionEntity
    ) {
        return new Section(
                sectionEntity.getId(),
                new Station(sectionEntity.getStartStationId(),
                        stationIdByStationName.get(sectionEntity.getStartStationId())),
                new Station(sectionEntity.getEndStationId(),
                        stationIdByStationName.get(sectionEntity.getEndStationId())),
                new Distance(sectionEntity.getDistance())
        );
    }
}
