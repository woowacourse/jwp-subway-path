package subway.adapter.out.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.adapter.out.persistence.dao.SectionDao;
import subway.adapter.out.persistence.entity.SectionEntity;
import subway.application.port.out.section.SectionCommandPort;
import subway.application.port.out.section.SectionQueryPort;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionJdbcAdapter implements SectionCommandPort , SectionQueryPort {
    private final SectionDao sectionDao;

    public SectionJdbcAdapter(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public void saveSection(final Long lineId, final List<Section> sections) {
        final List<SectionEntity> sectionEntities = SectionEntity.of(lineId, sections);
        sectionDao.saveSection(lineId,sectionEntities);
    }

    @Override
    public List<Section> findAllByLineId(final Long lineId) {
        return sectionDao.findAllByLineId(lineId).stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getLineId(),
                        new Station(sectionEntity.getUpStationName()),
                        new Station(sectionEntity.getDownStationName()),
                        sectionEntity.getDistance())
                ).collect(Collectors.toList());
    }
}
