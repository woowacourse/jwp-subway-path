package subway.application;

import org.springframework.stereotype.Component;
import subway.dao.SectionDao;
import subway.dao.dto.SectionStationResultMap;
import subway.dao.entity.SectionEntity;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public boolean exists(Long upStationId, Long downStationId) {
        return sectionDao.exists(upStationId, downStationId);
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
        final List<SectionStationResultMap> sectionEntities = sectionDao.findAllByLineId(lineId);
        final List<Section> sections = sectionEntities.stream()
                .map(this::mapFrom)
                .collect(toList());
        return Sections.from(sections);
    }

    protected Section mapFrom(SectionStationResultMap resultMap) {
        return new Section(
                resultMap.getSectionId(),
                new Distance(resultMap.getDistance()),
                new Station(resultMap.getUpStationId(), resultMap.getUpStationName()),
                new Station(resultMap.getDownStationId(), resultMap.getDownStationName()),
                resultMap.getLineId()
        );
    }
}
