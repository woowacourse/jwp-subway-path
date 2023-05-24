package subway.repository;

import org.springframework.stereotype.Component;
import subway.dao.SectionDao;
import subway.dao.SectionStationDao;
import subway.dao.dto.SectionStationResultMap;
import subway.dao.entity.SectionEntity;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.section.MultiLineSections;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;

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

    public Long insertAndUpdate(Section newSection, Section updateSection) {
        update(updateSection);
        return insert(newSection);
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

    public SingleLineSections findAllByLineId(Long lineId) {
        final List<SectionStationResultMap> resultMaps = sectionStationDao.findAllByLineId(lineId);
        final List<Section> sections = resultMaps.stream()
                .map(this::mapFrom)
                .collect(toList());
        return SingleLineSections.from(sections);
    }

    public MultiLineSections findAll() {
        final List<SectionStationResultMap> resultMaps = sectionStationDao.findAll();
        final List<Section> sections = resultMaps.stream()
                .map(this::mapFrom)
                .collect(toList());
        return MultiLineSections.from(sections);

    }

    private Section mapFrom(SectionStationResultMap resultMap) {
        return new Section(
                resultMap.getSectionId(),
                Distance.from(resultMap.getDistance()),
                new Station(resultMap.getUpStationId(), resultMap.getUpStationName()),
                new Station(resultMap.getDownStationId(), resultMap.getDownStationName()),
                resultMap.getLineId()
        );
    }
}
