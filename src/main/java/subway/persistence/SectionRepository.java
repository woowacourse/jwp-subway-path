package subway.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Section;
import subway.persistence.dto.SectionDto;

@Repository
public class SectionRepository {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionRepository(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Long save(Long lineId, Section section) {
        return sectionDao.insert(toDto(lineId, section));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(Long lineId, List<Section> sections) {
        deleteAllBy(lineId);
        sectionDao.insertAll(toDtos(lineId, sections));
    }

    public List<Section> findAllBy(Long lineId) {
        return toSections(sectionDao.findAllByLineId(lineId));
    }

    public void deleteAllBy(Long lineId) {
        sectionDao.deleteAllByLineId(lineId);
    }

    private SectionDto toDto(Long lineId, Section section) {
        return new SectionDto(
                lineId,
                section.getUpperStation().getId(),
                section.getLowerStation().getId(),
                section.getDistance()
        );
    }

    private List<SectionDto> toDtos(Long lineId, List<Section> sections) {
        return sections.stream()
                .map(section -> toDto(lineId, section))
                .collect(Collectors.toList());
    }

    private Section toSection(SectionDto sectionDto) {
        return new Section(
                stationDao.findById(sectionDto.getStationId()),
                stationDao.findById(sectionDto.getNextStationId()),
                sectionDto.getDistance()
        );
    }

    private List<Section> toSections(List<SectionDto> sectionDtos) {
        return sectionDtos.stream()
                .map(this::toSection)
                .collect(Collectors.toList());
    }
}
