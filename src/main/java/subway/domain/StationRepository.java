package subway.domain;

public interface StationRepository {
    Long save(Station station);

    Station findById(Long id);

    Long findIdByName(String name);

    void deleteById(Long id);

    boolean isExistStation(Station station);
}
