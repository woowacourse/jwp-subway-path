package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

import java.util.List;

@Repository
public interface LineRepository {

    LineEntity saveLine(LineEntity lineEntity);

    LineEntity findLineById(Long id);

    List<SectionEntity> findSectionsByLine(LineEntity lineEntity);

    StationEntity findStationById(Long id);

    StationEntity findStationByName(String stationName);

    List<LineEntity> findAllLines();

    void updateLine(LineEntity lineEntity);

    void deleteLineById(Long id);
}
