package subway.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.StationDao;
import subway.domain.station.Station;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Import({StationRepository.class, StationDao.class})
class StationRepositoryTest {

    @Autowired
    private StationRepository repository;

    @Autowired
    private StationDao dao;

    @Test
    void insert() {
        // given
        final Station station = new Station("A");

        // when
        final Station saved = repository.save(station);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo("A")
        );
    }

    @Test
    void findById() {
        // given
        final Station station = new Station("A");
        final Station saved = repository.save(station);

        // when
        final Optional<Station> stationOptional = repository.findById(saved.getId());

        // then
        final Station expected = stationOptional.orElseThrow();
        assertThat(saved).isEqualTo(expected);
    }
}
