package subway.application;

import static subway.application.StationFactory.toStation;

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
import subway.dto.SectionSaveDto;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final SectionsMapper sectionsMapper;

    public SectionService(SectionDao sectionDao, StationDao stationDao, final SectionsMapper sectionsMapper) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.sectionsMapper = sectionsMapper;
    }

    @Transactional
    public void saveSection(Long lineId, SectionSaveDto request) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        Sections sections = sectionsMapper.mapFrom(sectionEntities);

        Station requestStartStation = new Station(request.getStartStation());
        Station requestEndStation = new Station(request.getEndStation());
        saveNewStationIfNotExists(requestStartStation);
        saveNewStationIfNotExists(requestEndStation);
        Distance requestDistance = new Distance(request.getDistance());
        Section requestSection = new Section(requestStartStation, requestEndStation, requestDistance);

        sections.add(requestSection);

        updateSections(lineId, sections);
    }

    private void updateSections(Long lineId, Sections sections) {
        sectionDao.deleteAllByLineId(lineId);

        List<SectionEntity> makeSectionEntitiesByAddedSections = makeSectionEntities(lineId, sections.getSections());

        sectionDao.insertAll(makeSectionEntitiesByAddedSections);
    }

    private void saveNewStationIfNotExists(Station station) {
        if (!stationDao.isExistStationByName(station.getName())) {
            stationDao.insert(new StationEntity(station.getName()));
        }
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

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        List<SectionEntity> sectionEntitiesOfLine = sectionDao.findByLineId(lineId);
        Sections sections = sectionsMapper.mapFrom(sectionEntitiesOfLine);

        Station removedStation = toStation(stationDao.findById(stationId));
        sections.remove(removedStation);

        updateSections(lineId, sections);
    }

}
