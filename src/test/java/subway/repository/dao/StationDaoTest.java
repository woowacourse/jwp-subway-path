package subway.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.repository.dao.EntityFixtures.GANGNAM_STATION;
import static subway.repository.dao.EntityFixtures.YEOKSAM_STATION;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.StationEntity;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 역을_저장한다() {
        // given
        final StationEntity station = GANGNAM_STATION;

        // when
        final StationEntity saveStation = stationDao.insert(station);

        // then
        assertThat(saveStation.getName()).isEqualTo(station.getName());
        assertThat(saveStation.getId()).isNotNull();
    }

    @Test
    void 여러_역을_저장한다() {
        // given
        List<StationEntity> stations = List.of(GANGNAM_STATION, YEOKSAM_STATION);

        // when
        stationDao.insertAll(stations);

        // then
        assertThat(stationDao.findAll()).hasSize(stations.size());
    }

    @Test
    void 모든_역을_조회한다() {
        // given
        List<StationEntity> stations = List.of(GANGNAM_STATION, YEOKSAM_STATION);
        stationDao.insertAll(stations);

        // when
        final List<StationEntity> findStations = stationDao.findAll();

        // then
        assertThat(findStations)
                .extracting(StationEntity::getName)
                .containsExactlyElementsOf(stations.stream().map(StationEntity::getName).collect(Collectors.toList()));
    }

    @Test
    void ID를_기준으로_역을_조회한다() {
        // given
        final StationEntity saveStation = stationDao.insert(GANGNAM_STATION);

        // when
        final StationEntity findStation = stationDao.findById(saveStation.getId());

        // then
        assertThat(findStation).isEqualTo(saveStation);
    }

    @Test
    void 이름을_기준으로_역을_조회한다() {
        // given
        final StationEntity saveStation = stationDao.insert(GANGNAM_STATION);

        // when
        final Optional<StationEntity> findStation = stationDao.findByName(saveStation.getName());

        // then
        assertThat(findStation).isPresent();
        assertThat(findStation.get()).isEqualTo(saveStation);
    }

    @Test
    void ID를_기준으로_역을_삭제한다() {
        // given
        final StationEntity saveStation = stationDao.insert(GANGNAM_STATION);

        // when
        stationDao.deleteById(saveStation.getId());
        final Optional<StationEntity> findStation = stationDao.findByName(saveStation.getName());

        // then
        assertThat(findStation).isEmpty();
    }

    @Test
    void 역이_존재하면_true를_반환한다() {
        // given
        stationDao.insert(GANGNAM_STATION);

        // when
        final boolean exists = stationDao.existsByName(GANGNAM_STATION.getName());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 역이_존재하지_않으면_false를_반환한다() {
        // given
        stationDao.insert(GANGNAM_STATION);
        String nonExistStationName = "존재하지 않는 역";

        // when
        final boolean exists = stationDao.existsByName(nonExistStationName);

        // then
        assertThat(exists).isFalse();
    }
}
