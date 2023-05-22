package subway.infrastructure.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.application.core.domain.Station;
import subway.infrastructure.dao.StationDao;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class StationPersistenceAdapterTest {

    private StationPersistenceAdapter stationRepository;

    @Autowired
    public StationPersistenceAdapterTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        StationDao stationDao = new StationDao(jdbcTemplate, dataSource);
        stationRepository = new StationPersistenceAdapter(stationDao);
    }

    @Test
    @DisplayName("Station을 삽입할 수 있다")
    void insert() {
        // given
        Station station = new Station(null, "잠실역");

        // when
        Station inserted = stationRepository.insert(station);

        // then
        assertThat(inserted).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(station);
    }

    @Test
    @DisplayName("Station을 모두 찾을 수 있다")
    void findAll() {
        // given
        stationRepository.insert(new Station(null, "잠실역"));
        stationRepository.insert(new Station(null, "방배역"));

        // when
        List<Station> found = stationRepository.findAll();

        // then
        assertThat(found).hasSize(2);
    }

    @Test
    @DisplayName("Station을 ID를 통해 찾을 수 있다")
    void findById() {
        // given
        Station inserted = stationRepository.insert(new Station(null, "잠실역"));

        // when
        Station found = stationRepository.findById(inserted.getId());

        // then
        assertThat(found).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(inserted);
    }

    @Test
    @DisplayName("Station을 업데이트 할 수 있다")
    void update() {
        // given
        Station inserted = stationRepository.insert(new Station(null, "잠실역"));

        // when
        Station updated = new Station(inserted.getId(), "방배역");
        stationRepository.update(updated);
        Station found = stationRepository.findById(inserted.getId());

        // then
        assertThat(found).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(updated);
    }

    @Test
    @DisplayName("Station을 삭제할 수 있다")
    void deleteById() {
        // given
        Station inserted = stationRepository.insert(new Station(null, "잠실역"));

        // when
        stationRepository.deleteById(inserted.getId());
        List<Station> found = stationRepository.findAll();

        // then
        assertThat(found).isEmpty();
    }
}
