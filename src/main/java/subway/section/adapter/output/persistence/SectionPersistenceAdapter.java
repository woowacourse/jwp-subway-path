package subway.section.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import subway.section.application.port.output.SaveSectionPort;
import subway.section.domain.Section;
import subway.station.adapter.output.persistence.StationDao;
import subway.station.adapter.output.persistence.StationEntity;

@RequiredArgsConstructor
@Repository
public class SectionPersistenceAdapter implements SaveSectionPort {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    
    @Override
    public Long save(final Section section, final Long lineId) { // TODO
        final StationEntity leftStationEntity = stationDao.findByName(section.getLeft());
        final StationEntity rightStationEntity = stationDao.findByName(section.getRight());
        
        final SectionEntity sectionEntity = new SectionEntity(
                leftStationEntity.getId(),
                rightStationEntity.getId(),
                section.getDistance().getDistance(),
                lineId
        );
        return sectionDao.insert(sectionEntity);
    }
}
