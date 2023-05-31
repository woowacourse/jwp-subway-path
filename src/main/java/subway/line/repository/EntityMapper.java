package subway.line.repository;

import subway.line.domain.Line;
import subway.line.domain.MiddleSection;
import subway.line.entity.LineEntity;
import subway.line.entity.SectionEntity;
import subway.station.domain.Station;
import subway.station.entity.StationEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

class EntityMapper {

    public static LineEntity toLineEntity(Line line) {
        return new LineEntity.Builder().name(line.getName())
                                       .additionalFare(line.getAdditionalFare())
                                       .build();
    }

    public static List<SectionEntity> toSectionEntities(Line line, long lineId) {
        return line.getSections()
                   .stream()
                   .map(section -> toSectionEntity(section, lineId))
                   .collect(Collectors.toUnmodifiableList());
    }

    private static SectionEntity toSectionEntity(MiddleSection section, long lineId) {
        Station upstream = section.getUpstream();
        Station downstream = section.getDownstream();

        return new SectionEntity.Builder().upstreamId(upstream.getId())
                                          .downstreamId(downstream.getId())
                                          .distance(section.getDistance())
                                          .lineId(lineId)
                                          .build();
    }

    public static Line toLine(LineEntity lineEntity, List<SectionEntity> sectionEntitiesOfLine, List<StationEntity> allStationEntities) {
        return sectionEntitiesOfLine.stream()
                                    .map(sectionEntity -> toMiddleSection(sectionEntity, allStationEntities))
                                    .collect(collectingAndThen(
                                            toList(),
                                            (sections) -> new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getAdditionalFare(), sections)
                                    ));
    }

    private static MiddleSection toMiddleSection(SectionEntity sectionEntity, List<StationEntity> stationEntities) {
        Station upstream = findStationById(sectionEntity.getUpstreamId(), stationEntities);
        Station downstream = findStationById(sectionEntity.getDownstreamId(), stationEntities);

        return new MiddleSection(upstream, downstream, sectionEntity.getDistance());
    }

    private static Station findStationById(long stationId, List<StationEntity> stationEntities) {
        return stationEntities.stream()
                              .filter(stationEntity -> stationEntity.getId() == stationId)
                              .findFirst()
                              .map(stationEntity -> new Station(stationId, stationEntity.getName()))
                              .orElseThrow(() -> new NoSuchElementException("디버깅: Section에 존재하지만 Station 테이블에 없는 id입니다. id: " + stationId));
    }
}
