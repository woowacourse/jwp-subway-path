package subway.dao;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

@Repository
public class StationDao {

    public StationEntity save(final StationEntity stationEntity) {
        return null;
    }

    public Optional<StationEntity> findById(final Long id) {
        return Optional.empty();
    }
}
