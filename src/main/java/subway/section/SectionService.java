package subway.section;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.section.domain.SectionsStationDtoSorter;
import subway.section.dto.SectionDeleteDto;
import subway.section.dto.SectionStationDto;
import subway.section.persistence.SectionDao;
import subway.section.persistence.SectionEntity;
import subway.station.domain.Station;
import subway.station.persistence.StationDao;
import subway.station.persistence.StationEntity;

@Service
@Transactional
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void addSection(
        final SectionEntity sectionEntity,
        final String upStationName,
        final String downStationName
    ) {
        final Sections sections = generateSections(sectionEntity.getLineId());
        sections.addSection(upStationName, downStationName, sectionEntity.getDistance());
        updateLine(sectionEntity.getLineId(), sections);
    }

    private Sections generateSections(final Long lineId) {
        final List<SectionStationDto> sortedSectionsStationDtoByLineId = findSortedSectionStationDtoByLineId(
            lineId);
        final List<Section> sortedSections = sortedSectionsStationDtoByLineId.stream()
            .map((sectionStationDto) -> Section.of(
                Station.register(sectionStationDto.getUpStationName()),
                Station.register(sectionStationDto.getDownStationName()),
                sectionStationDto.getDistance()
            )).collect(Collectors.toList());

        return Sections.values(sortedSections);
    }

    private void updateLine(final Long lineId, final Sections sections) {
        sectionDao.deleteAlByLineId(lineId);
        sections.getSections().forEach((section) -> sectionDao.insert(
                new SectionEntity(
                    lineId,
                    stationDao.findIdByName(section.getUp().getName()),
                    stationDao.findIdByName(section.getDown().getName()),
                    section.getDistance())
            )
        );
    }

    public List<SectionStationDto> findSortedSectionStationDtoByLineId(final Long lineId) {
        final List<SectionStationDto> sectionsInLine = sectionDao.findAllByLineId(lineId);
        final SectionsStationDtoSorter sectionsStationDtoSorter = SectionsStationDtoSorter.use(sectionsInLine);
        return sectionsStationDtoSorter.getSortedSectionStationsDto();
    }

    public List<StationEntity> findSortedStationEntityByLineId(final Long lineId) {
        final List<SectionStationDto> sectionsInLine = sectionDao.findAllByLineId(lineId);
        final SectionsStationDtoSorter sectionsStationDtoSorter = SectionsStationDtoSorter.use(sectionsInLine);
        return sectionsStationDtoSorter.getSortedStationEntities();
    }

    public void removeStationBy(final SectionDeleteDto sectionDeleteDto) {
        final Sections sections = generateSections(sectionDeleteDto.getLineId());
        sections.removeStation(sectionDeleteDto.getStationName());
        updateLine(sectionDeleteDto.getLineId(), sections);
    }
}
