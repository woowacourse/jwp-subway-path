package subway.dao;

import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class SubwayMapRepository {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SubwayMapRepository(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public SubwayMap findByLineId(final Long id) {
        final List<Station> stations = stationDao.findAll();
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);
        return generateMap(stations, sectionEntities);
    }

    private SubwayMap generateMap(final List<Station> stations, final List<SectionEntity> sectionEntities) {
        final Map<Station, List<Section>> subwayMap = stations.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        station -> sectionEntities.stream()
                                .filter(sectionEntity -> sectionEntity.getDepartureId() == station.getId())
                                .map(entity -> new Section(entity.getDistance(), entity.getDepartureId(), entity.getArrivalId(), entity.getDirection()))
                                .collect(Collectors.toList())
                ));

        return new SubwayMap(subwayMap);
    }
}
