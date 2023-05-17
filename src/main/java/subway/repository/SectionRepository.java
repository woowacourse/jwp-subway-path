package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationRepository stationRepository;

    public SectionRepository(SectionDao sectionDao, StationRepository stationRepository) {
        this.sectionDao = sectionDao;
        this.stationRepository = stationRepository;
    }

    public List<Section> findByLineId(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        return sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getId(),
                        stationRepository.findById(sectionEntity.getLeftStationId()),
                        stationRepository.findById(sectionEntity.getRightStationId()),
                        sectionEntity.getDistance()))
                .collect(Collectors.toList());
    }

    public void deleteByLineId(Long lineId) {
        sectionDao.deleteByLineId(lineId);
    }

    public void insertInLine(Line line) {
        line.getSections().getSections().forEach(
                (aLong, section) -> sectionDao.insert(
                        new SectionEntity(
                                section.getLeftStation().getId(),
                                section.getRightStation().getId(),
                                section.getDistance().getDistance(),
                                line.getId()))
        );
    }
}
