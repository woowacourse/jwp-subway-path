package subway.section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.persistence.LineDao;
import subway.line.persistence.LineEntity;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.section.domain.SectionsSorter;
import subway.section.dto.SectionCreateDto;
import subway.section.persistence.SectionDao;
import subway.section.persistence.SectionEntity;
import subway.station.domain.Station;
import subway.station.dto.StationResponseDto;
import subway.station.persistence.StationDao;
import subway.station.persistence.StationEntity;

@Service
@Transactional
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(final SectionDao sectionDao,
        StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public void addSection(final SectionCreateDto sectionCreateDto) {
        final LineEntity lineEntity = lineDao.findById(sectionCreateDto.getLineId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선 이름입니다."));
        final StationEntity upStationEntity = stationDao.findById(sectionCreateDto.getUpStationId())
            .orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 존재하지 않습니다."));
        final StationEntity downStationEntity = stationDao.findById(sectionCreateDto.getDownStationId())
            .orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 존재하지 않습니다."));

        final Section section = createSection(sectionCreateDto, upStationEntity, downStationEntity);

        final Line line = new Line(lineEntity.getId(), lineEntity.getLineName(), generateSections(lineEntity.getId()));
        addSectionByCondition(section, line);
        updateLine(line);
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

    private void addSectionByCondition(Section section, Line line) {
        if (line.isEmpty()) {
            line.initializeLine(section.getUpStation(), section.getDownStation(), section.getDistance());
            return;
        }
        line.addStation(section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    private void updateLine(Line line) {
        sectionDao.deleteAllByLineId(line.getId());
        line.getSections().forEach((section) -> sectionDao.insert(
                new SectionEntity(
                    line.getId(),
                    section.getUpStation().getId(),
                    section.getDownStation().getId(),
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

    public void removeStationBy(final Long lineId, final Long stationId) {
        final LineEntity lineEntity = lineDao.findById(lineId)
            .orElseThrow(() -> new IllegalArgumentException("해당 라인은 존재하지 않습니다."));
        final StationEntity stationEntity = stationDao.findById(stationId)
            .orElseThrow(() -> new IllegalArgumentException("해당 역은 존재하지 않습니다."));

        final Line line = new Line(lineEntity.getId(), lineEntity.getLineName(), generateSections(lineEntity.getId()));
        line.removeStation(new Station(stationEntity.getId(), stationEntity.getStationName()));
        updateLine(line);
    }
}
