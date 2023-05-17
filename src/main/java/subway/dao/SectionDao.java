package subway.dao;

import java.util.List;
import java.util.Optional;
import subway.dao.entity.SectionEntity;
import subway.domain.section.Section;

public interface SectionDao {

    long insert(Long lineId, Section section);

    void update(Long sectionId, Section section);

    Long countByLineId(Long lineId);

    List<SectionEntity> findAllByLineId(Long lineId);

    boolean isStationInLine(Long lineId, String stationName);

    boolean isEmptyByLineId(Long lineId);

    Optional<SectionEntity> findByStartStationNameAndLineId(String upBoundStationName, Long lineId);

    Optional<SectionEntity> findByEndStationNameAndLineId(String downBoundStationName, Long lineId);

    void deleteBy(SectionEntity sectionEntity);

    List<SectionEntity> findAll();

    boolean doesNotExistByStationName(String stationName);
}
