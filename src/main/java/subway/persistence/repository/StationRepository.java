package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;

@Repository
public interface StationRepository {
    LineEntity findLineById(Long id);

    void deleteAllSection();

    void saveAllSection(List<SectionEntity> sectionEntities);

    StationEntity saveStation(StationEntity station);

    List<StationEntity> findAllStation();

    StationEntity findStationByName(String stationName);

    List<SectionEntity> findAllSectionByLineId(Long lineId);

    StationEntity findStationById(Long id);

    void deleteAllStation();

    void deleteStationById(Long id);

    void updateStation(StationEntity stationEntity);
}
