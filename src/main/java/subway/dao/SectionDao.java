package subway.dao;

import subway.entity.SectionEntity;

import java.util.List;

public interface SectionDao {
    SectionEntity insert(SectionEntity sectionEntity);

    List<SectionEntity> findAll();

    void delete(Long lineId, Long upStationId, Long downStationId);

    List<SectionEntity> findBy(Long lineId);

    Long findStationIdBefore(Long lineId, Long stationId);

    Long findStationIdAfter(Long lineId, Long stationId);

    void deleteSectionsOf(Long lineId);
}
