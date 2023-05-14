package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.*;
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

        List<Section> sectionsOfLine = sectionEntities.stream()
                .map(this::toSection)
                .collect(Collectors.toList());

        Sections sections = new Sections(sectionsOfLine);

        Section requestSection = new Section(
                new Station(request.getStartStation()),
                new Station(request.getEndStation()),
                new Distance(request.getDistance()));

        sections.add(requestSection);
        addStationOfSection(requestSection.getStartStation(), requestSection.getEndStation());

        sectionDao.deleteAllById(lineId);
        sectionDao.insertAll(toSectionEntities(lineId, sections.getSections()));
    }

    private void addStationOfSection(Station requestStartStation, Station requestEndStation) {
        if (!stationDao.isExistStationByName(requestStartStation.getName())) {
            stationDao.insert(new StationEntity(requestStartStation.getName()));
        }

        if (!stationDao.isExistStationByName(requestEndStation.getName())) {
            stationDao.insert(new StationEntity(requestEndStation.getName()));
        }
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

        sectionDao.deleteAllById(lineId);
        sectionDao.insertAll(toSectionEntities(lineId, sections.getSections()));

        deleteStation(stationId);
    }
    private void deleteStation(Long stationId) {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        boolean hasStartStation = sectionEntities.stream()
                .noneMatch(it -> it.getStartStationId().equals(stationId));
        boolean hasEndStation = sectionEntities.stream()
                .noneMatch(it -> it.getEndStationId().equals(stationId));

        if (hasStartStation && hasEndStation) {
            stationDao.deleteById(stationId);
        }
    }


    private List<SectionEntity> toSectionEntities(Long lineId, List<Section> sections) {
        return sections.stream()
                .map(it -> {
                    Long startStationId = stationDao.findIdByName(it.getStartStation().getName());
                    Long endStationId = stationDao.findIdByName(it.getEndStation().getName());

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

    private Station toStation(StationEntity stationEntity) {
        return new Station(stationEntity.getName());
    }
}
