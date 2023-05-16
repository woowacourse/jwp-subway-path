package subway.domain;

import java.util.List;

public interface StationRepository {
    Long save(Station station);

    List<Station> findAll();

    Station findById(Long id);

    Long findIdByName(String name);

    void deleteById(Long id);

    boolean isExistStation(Station station);
}
