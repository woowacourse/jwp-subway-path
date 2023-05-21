package subway.repository;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class LineMapper {

    public Line toLine(
            LineEntity lineEntity,
            List<SectionEntity> sectionEntities,
            List<StationEntity> stationEntities) {
        Map<Long, StationEntity> stationsById = stationEntities.stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        stationEntity -> stationEntity));

        List<Section> sections = toSections(sectionEntities, stationsById);

        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                sections);
    }

    public List<Section> toSections(List<SectionEntity> sectionEntities, Map<Long, StationEntity> stationsById) {
        return sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getId(),
                        sectionEntity.getLineId(),
                        toStation(sectionEntity.getUpStationId(), stationsById),
                        toStation(sectionEntity.getDownStationId(), stationsById),
                        new Distance(sectionEntity.getDistance()))
                )
                .collect(toList());
    }

    private Station toStation(long stationId, Map<Long, StationEntity> stationsById) {
        StationEntity stationEntity = stationsById.get(stationId);
        return new Station(
                stationEntity.getId(),
                stationEntity.getName());
    }

    public SectionEntity toSectionEntity(Section section) {
        return new SectionEntity(
                section.getLineId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance());
    }
}
