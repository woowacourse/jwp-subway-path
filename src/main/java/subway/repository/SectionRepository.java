package subway.repository;

import org.springframework.stereotype.Component;
import subway.dao.SectionDao;
import subway.dao.SectionStationDao;
import subway.dao.dto.SectionStationResultMap;
import subway.dao.entity.SectionEntity;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class SectionRepository {

    private final SectionDao sectionDao;
    private final SectionStationDao sectionStationDao;

    public SectionRepository(SectionDao sectionDao, SectionStationDao sectionStationDao) {
        this.sectionDao = sectionDao;
        this.sectionStationDao = sectionStationDao;
    }

    public Long insert(Section section) {
        final SectionEntity sectionEntity = mapToEntity(section);
        return sectionDao.insert(sectionEntity);
    }

    public void update(Section newSection) {
        final SectionEntity sectionEntity = mapToEntity(newSection);
        sectionDao.update(sectionEntity);
    }

    protected SectionEntity mapToEntity(Section section) {
        return new SectionEntity(
                section.getDistance().getDistance(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getLineId()
        );
    }

    public void delete(Long sectionId) {
        sectionDao.delete(sectionId);
    }

    public Sections findAllByLineId(Long lineId) {
        final List<SectionStationResultMap> sectionEntities = sectionStationDao.findAllByLineId(lineId);
        final List<Section> sections = sectionEntities.stream()
                .map(this::mapFrom)
                .collect(toList());
        return Sections.from(sections);
    }

    protected Section mapFrom(SectionStationResultMap resultMap) {
        return new Section(
                resultMap.getSectionId(),
                Distance.from(resultMap.getDistance()),
                new Station(resultMap.getUpStationId(), resultMap.getUpStationName()),
                new Station(resultMap.getDownStationId(), resultMap.getDownStationName()),
                resultMap.getLineId()
        );
    }

    public List<Sections> findAll() {
        return null;
    }
}
