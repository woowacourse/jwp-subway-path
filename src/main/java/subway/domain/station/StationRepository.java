package subway.domain.station;

import java.util.List;
import subway.domain.station.dto.StationRes;

public interface StationRepository {

    Long insert(final Station station);

    List<StationRes> findAll();

    Station findById(final Long id);

    void update(final Long id, final Station station);

    void deleteById(final Long id);

    boolean existByName(final String name);
}
