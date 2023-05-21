package subway.application;

import static subway.application.StationFactory.toStation;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionEntity;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

@Transactional(readOnly = true)
@Component
public class SectionsMapper {

    private final StationDao stationDao;

    public SectionsMapper(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Sections mapFrom(List<SectionEntity> sectionEntities) {
        List<Section> sections = sectionEntities.stream()
                .map(this::toSection)
                .collect(Collectors.toList());

        return new Sections(sections);
    }

    private Section toSection(SectionEntity sectionEntity) {
        Station startStation = toStation(stationDao.findById(sectionEntity.getStartStationId()));
        Station endStation = toStation(stationDao.findById(sectionEntity.getEndStationId()));
        Distance distance = new Distance(sectionEntity.getDistance());

        return new Section(startStation, endStation, distance);
    }

}
