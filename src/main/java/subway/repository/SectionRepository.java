package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.entity.SectionEntity;
import subway.entity.SectionStationEntity;
import subway.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private static final int UPBOUND_STATION_INDEX = 0;
    private static final int DOWNBOUND_STATION_INDEX = 1;
    private static final int LAST_SECTION_INDEX = 0;

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public List<Station> saveInitStations(final Section section, final Long lineId) {
        List<StationEntity> stationEntities = List.of(
                new StationEntity(section.getLeftStation().getName(), lineId),
                new StationEntity(section.getRightStation().getName(), lineId));
        List<StationEntity> insertedStationEntities = stationDao.insertInit(stationEntities);

        sectionDao.insert(new SectionEntity(insertedStationEntities.get(UPBOUND_STATION_INDEX).getId(),
                insertedStationEntities.get(DOWNBOUND_STATION_INDEX).getId(),
                lineId,
                section.getDistance()));

        return List.of(
                new Station(insertedStationEntities.get(UPBOUND_STATION_INDEX).getId(),
                        insertedStationEntities.get(UPBOUND_STATION_INDEX).getName()),
                new Station(insertedStationEntities.get(DOWNBOUND_STATION_INDEX).getId(),
                        insertedStationEntities.get(DOWNBOUND_STATION_INDEX).getName()));
    }

    public Sections readAllSection() {
        List<SectionStationEntity> sectionStationEntities = sectionDao.findAll();
        return new Sections(getSections(sectionStationEntities));
    }

    public void updateSectionAndDeleteStation(final Long lineId, final Sections updateSections, final Station station) {
        List<Section> existSections = getSections(sectionDao.findByLineId(lineId));
        Sections sections = new Sections(existSections);
        if (updateSections.isEmpty()) {
            List<Long> stationIds = List.of(sections.findUpBoundStation().getId(), sections.findDownBoundStation().getId());
            stationDao.deleteBothById(stationIds);
            sectionDao.deleteById(existSections.get(LAST_SECTION_INDEX).getId());
            return;
        }
        deleteUpdateSection(updateSections, lineId);
        addUpdateSection(updateSections, lineId);
        stationDao.deleteById(station.getId());
    }

    public void updateSection(final Sections sections, final Long lineId) {
        deleteUpdateSection(sections, lineId);
        addUpdateSection(sections, lineId);
    }

    private void deleteUpdateSection(final Sections sections, final Long lineId) {
        List<Section> updatedSections = sections.getSections();
        List<Section> existedSections = getSections(sectionDao.findByLineId(lineId));
        existedSections.removeAll(updatedSections);

        List<Long> sectiondIds = existedSections.stream()
                .map(Section::getId).
                collect(Collectors.toList());

        if (sectiondIds.isEmpty()) {
            return;
        }
        sectionDao.deleteBothById(sectiondIds);
    }

    private void addUpdateSection(final Sections sections, final Long lineId) {
        List<Section> updatedSections = sections.getSections();
        List<Section> existedSections = getSections(sectionDao.findByLineId(lineId));
        updatedSections.removeAll(existedSections);

        sectionDao.insertBoth(updatedSections.stream()
                .map(section -> new SectionEntity(
                        section.getLeftStation().getId(),
                        section.getRightStation().getId(),
                        lineId, section.getDistance())).
                collect(Collectors.toList()));
    }

    private List<Section> getSections(List<SectionStationEntity> sectionStationEntities) {
        List<Section> sections = new ArrayList<>();
        for (SectionStationEntity sectionStationEntity : sectionStationEntities) {
            sections.add(new Section(
                    sectionStationEntity.getId(),
                    new Station(sectionStationEntity.getLeftStationId(), sectionStationEntity.getLeftStationName()),
                    new Station(sectionStationEntity.getRightStationId(), sectionStationEntity.getRightStationName()),
                    sectionStationEntity.getDistance())
            );
        }
        return sections;
    }
}
