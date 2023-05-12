package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

@Repository
public class StationRepositoryImpl implements StationRepository {

    @Override
    public Station insert(final Station station) {
        return null;
    }

    @Override
    public List<Station> findAll() {
        return null;
    }

    @Override
    public Station findById(final Long id) {
        return null;
    }

    @Override
    public void update(final Station newStation) {

    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public Optional<Station> findByName(final String stationName) {
        return Optional.empty();
    }
}
