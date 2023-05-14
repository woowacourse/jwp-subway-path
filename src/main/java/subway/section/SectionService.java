package subway.section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.LineService;
import subway.line.persistence.LineEntity;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.section.domain.SectionsSorter;
import subway.section.dto.SectionCreateDto;
import subway.section.dto.SectionDeleteDto;
import subway.section.persistence.SectionDao;
import subway.section.persistence.SectionEntity;
import subway.station.StationService;
import subway.station.domain.Station;
import subway.station.dto.StationResponseDto;
import subway.station.persistence.StationDao;
import subway.station.persistence.StationEntity;

@Service
@Transactional
public class SectionService {

    private final LineService lineService;
    private final StationService stationService;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(final LineService lineService, final StationService stationService, final SectionDao sectionDao,
        StationDao stationDao) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void addSection(final SectionCreateDto sectionCreateDto) {
        final LineEntity lineEntity = lineService.findById(sectionCreateDto.getLineId());
        final StationEntity upStationEntity = stationService.findById(sectionCreateDto.getUpStationId());
        final StationEntity downStationEntity = stationService.findById(sectionCreateDto.getDownStationId());

        final Section section = createSection(sectionCreateDto, upStationEntity, downStationEntity);

        final Sections sections = generateSections(lineEntity.getId());
        addSectionByCondition(section, sections);
        updateLine(lineEntity.getId(), sections);
    }

    private static Section createSection(SectionCreateDto sectionCreateDto, StationEntity upStationEntity,
        StationEntity downStationEntity) {
        final Station upStation = new Station(upStationEntity.getId(), upStationEntity.getStationName());
        final Station downStation = new Station(downStationEntity.getId(), downStationEntity.getStationName());
        return Section.of(upStation, downStation, sectionCreateDto.getDistance());
    }

    private Sections generateSections(final Long lineId) {
        final List<Section> sortedSectionsByLineId = findSortedSectionStationDtoByLineId(lineId);
        if (sortedSectionsByLineId.isEmpty()) {
            return Sections.empty();
        }
        return Sections.values(sortedSectionsByLineId);
    }

    private List<Section> findSortedSectionStationDtoByLineId(final Long lineId) {
        final List<Section> sectionsInLine = mapToSections(lineId);

        final SectionsSorter sectionsSorter = SectionsSorter.use(sectionsInLine);
        return sectionsSorter.getSortedSections();
    }

    private List<Section> mapToSections(Long lineId) {
        return sectionDao.findAllByLineId(lineId).stream()
            .map(sectionStationDto -> Section.of(
                new Station(sectionStationDto.getUpStationId(), sectionStationDto.getUpStationName()),
                new Station(sectionStationDto.getDownStationId(), sectionStationDto.getDownStationName())
                , sectionStationDto.getDistance())).collect(Collectors.toList());
    }

    private void addSectionByCondition(Section section, Sections sections) {
        if (sections.isEmpty()) {
            sections.initializeSections(section.getUpStation(), section.getDownStation(), section.getDistance());
            return;
        }
        sections.addSection(section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    private void updateLine(final Long lineId, final Sections sections) {
        sectionDao.deleteAllByLineId(lineId);
        sections.getSections().forEach((section) -> sectionDao.insert(
                new SectionEntity(
                    lineId,
                    stationDao.findIdByName(section.getUpStation().getName()),
                    stationDao.findIdByName(section.getDownStation().getName()),
                    section.getDistance())
            )
        );
    }

    public List<StationResponseDto> findSortedStations(final Long lineId) {
        final SectionsSorter sectionsSorter = SectionsSorter.use(mapToSections(lineId));
        final List<Section> sortedSections = sectionsSorter.getSortedSections();

        return mapToStationResponseDtos(sortedSections);
    }

    private static List<StationResponseDto> mapToStationResponseDtos(List<Section> sortedSections) {
        final List<StationResponseDto> stationResponseDtos = new ArrayList<>();

        for (Section sortedSection : sortedSections) {
            final Station upStation = sortedSection.getUpStation();
            stationResponseDtos.add(new StationResponseDto(upStation.getId(), upStation.getName()));
        }

        final Station lastStation = sortedSections.get(sortedSections.size() - 1).getDownStation();
        stationResponseDtos.add(new StationResponseDto(lastStation.getId(), lastStation.getName()));

        return stationResponseDtos;
    }

    public void removeStationBy(final SectionDeleteDto sectionDeleteDto) {
        final Sections sections = generateSections(sectionDeleteDto.getLineId());
        sections.removeStation(sectionDeleteDto.getStationName());
        updateLine(sectionDeleteDto.getLineId(), sections);
    }
}
