package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.dao.StationDao;
import subway.dao.StationEntity;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionRequest;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest request) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        List<Section> savedSections = sectionEntities.stream()
                .map(this::toSection)
                .collect(Collectors.toList());

        Sections sections = new Sections(savedSections);

        Station requestStartStation = new Station(request.getStartStation());
        Station requestEndStation = new Station(request.getEndStation());
        if (!stationDao.isExistStationByName(requestStartStation.getName())) {
            stationDao.insert(new StationEntity(requestStartStation.getName()));
        }

        if (!stationDao.isExistStationByName(requestEndStation.getName())) {
            stationDao.insert(new StationEntity(requestEndStation.getName()));
        }
        Distance requestDistance = new Distance(request.getDistance());
        Section requestSection = new Section(requestStartStation, requestEndStation, requestDistance);

        sections.add(requestSection);

        sectionDao.deleteAll();

        List<SectionEntity> makeSectionEntitiesByAddedSections = makeSectionEntities(lineId, sections.getSections());

        sectionDao.insertAll(makeSectionEntitiesByAddedSections);
    }

    private List<SectionEntity> makeSectionEntities(Long lineId, List<Section> sections) {
        return sections.stream()
                .map(it -> {
                    Station startStation = it.getStartStation();
                    Station endStation = it.getEndStation();

                    Long startStationId = stationDao.findIdByName(startStation.getName());
                    Long endStationId = stationDao.findIdByName(endStation.getName());

                    return new SectionEntity(lineId, startStationId, endStationId, it.getDistance().getDistance());
                })
                .collect(Collectors.toList());
    }


    private Section toSection(SectionEntity sectionEntity) {
        Station startStation = toStation(stationDao.findById(sectionEntity.getStartStationId()));
        Station endStation = toStation(stationDao.findById(sectionEntity.getEndStationId()));
        Distance distance = new Distance(sectionEntity.getDistance());

        return new Section(startStation, endStation, distance);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        List<SectionEntity> sectionEntitiesOfLine = sectionDao.findByLineId(lineId);

        List<Section> sectionsOfLine = sectionEntitiesOfLine.stream()
                .map(this::toSection)
                .collect(Collectors.toList());

        Sections sections = new Sections(sectionsOfLine);

        Station removedStation = toStation(stationDao.findById(stationId));
        sections.remove(removedStation);

        sectionDao.deleteAll();

        List<SectionEntity> makeSectionEntitiesByAddedSections = makeSectionEntities(lineId, sections.getSections());

        sectionDao.insertAll(makeSectionEntitiesByAddedSections);
    }

    private Station toStation(StationEntity stationEntity) {
        return new Station(stationEntity.getName());
    }
}
