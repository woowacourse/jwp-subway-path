package subway.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void insert(final Line line) {
        sectionDao.deleteAllByLineId(line.getId());

        final Map<Station, Section> sections = line.getSections().sections();
        final List<SectionEntity> sectionEntities = new ArrayList<>();

        for (Entry<Station, Section> sectionEntry : sections.entrySet()) {
            sectionEntities.addAll(mapToEntities(sectionEntry.getKey(), sectionEntry.getValue(), line.getId()));
        }

        sectionDao.insertAll(sectionEntities);
    }

    private List<SectionEntity> mapToEntities(final Station sourceStation, final Section section, final Long lineId) {
        final List<Station> stations = section.findAllStation();

        return stations.stream()
                .filter(station -> section.findDirectionByStation(station).matches(Direction.DOWN))
                .map(station -> SectionEntity.Builder.builder()
                        .lineId(lineId)
                        .upStationId(sourceStation.getId())
                        .downStationId(station.getId())
                        .distance(section.findDistanceByStation(station).getDistance())
                        .build()
                ).collect(Collectors.toList());
    }

    public Line findAllByLine(final Line line) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(line.getId());

        if (sectionEntities.isEmpty()) {
            return line;
        }

        final Set<Long> stationIds = mapToStationIds(sectionEntities);
        final Map<Long, Station> stations = stationDao.findAllByIds(stationIds)
                .stream()
                .collect(Collectors.toMap(StationEntity::getId, StationEntity::to));
        final Map<Station, Section> sections = mapToSection(sectionEntities, stations);

        return Line.of(line, Sections.of(sections));
    }

    private Map<Station, Section> mapToSection(final List<SectionEntity> sectionEntities, final Map<Long, Station> stations) {
        final Map<Station, Section> sectionInfos = new HashMap<>();

        for (SectionEntity sectionEntity : sectionEntities) {
            final Station upStation = stations.get(sectionEntity.getUpStationId());
            final Station downStation = stations.get(sectionEntity.getDownStationId());
            final Distance distance = Distance.from(sectionEntity.getDistance());

            final Section upSectionInfos = sectionInfos.getOrDefault(upStation, Section.create());
            upSectionInfos.add(downStation, distance, Direction.DOWN);
            sectionInfos.put(upStation, upSectionInfos);

            final Section downSectionInfos = sectionInfos.getOrDefault(downStation, Section.create());
            downSectionInfos.add(upStation, distance, Direction.UP);
            sectionInfos.put(downStation, downSectionInfos);
        }
        return sectionInfos;
    }

    private Set<Long> mapToStationIds(final List<SectionEntity> sectionEntities) {
        Set<Long> stationIds = new HashSet<>();

        for (SectionEntity sectionEntity : sectionEntities) {
            stationIds.add(sectionEntity.getUpStationId());
            stationIds.add(sectionEntity.getDownStationId());
        }
        return stationIds;
    }
}
