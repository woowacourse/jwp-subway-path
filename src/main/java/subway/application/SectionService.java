package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectionService {
    private final StationService stationService;
    private final SectionDao sectionDao;

    public SectionService(final StationService stationService, final SectionDao sectionDao) {
        this.stationService = stationService;
        this.sectionDao = sectionDao;
    }

    @Transactional(readOnly = true)
    public List<Section> findSectionsById(final long lineId) {
        final List<SectionEntity> sectionEntities = sectionDao.findById(lineId);
        return sectionBy(sectionEntities);
    }

    private List<Section> sectionBy(final List<SectionEntity> sectionEntities) {
        final List<Station> stations = stationService.findStationsOf(allStationNamesOf(sectionEntities));
        return sectionEntities.stream()
                .map(entity -> toSection(entity, stations))
                .collect(Collectors.toList());
    }

    private Set<String> allStationNamesOf(final List<SectionEntity> sectionEntities) {
        final HashSet<String> stationNames = new HashSet<>();
        for (final SectionEntity sectionEntity : sectionEntities) {
            stationNames.add(sectionEntity.getLeft());
            stationNames.add(sectionEntity.getRight());
        }
        return stationNames;
    }

    private Section toSection(final SectionEntity sectionEntity, final List<Station> stations) {
        final Station leftStation = stationOf(stations, station -> station.getName().equals(sectionEntity.getLeft()));
        final Station rightStation = stationOf(stations, station -> station.getName().equals(sectionEntity.getRight()));
        return new Section(leftStation, rightStation, new Distance(sectionEntity.getDistance()));
    }

    private Station stationOf(final List<Station> stations, final Predicate<Station> predicate) {
        return stations.stream()
                .filter(predicate)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Section 에 등록된 역이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<Section> findAll() {
        return sectionBy(sectionDao.findAll());
    }

    public void save(final Line line) {
        sectionDao.save(SectionEntity.toEntities(line.getId(), line.getSections()));
    }
}
