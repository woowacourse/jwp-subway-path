package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.entity.StationEntity;

@JdbcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class StationDaoTest {

    StationDao stationDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired DataSource dataSource) {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    void insert_메소드는_station을_저장하고_저장한_데이터를_반환한다() {
        final StationEntity stationEntity = StationEntity.from("12역");

        final StationEntity actual = stationDao.insert(stationEntity);

        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getName()).isEqualTo(stationEntity.getName())
        );
    }

    @Test
    void findById_메소드는_저장되어_있는_id를_전달하면_해당_station을_반환한다() {
        final StationEntity stationEntity = StationEntity.from("12역");
        final StationEntity persistStationEntity = stationDao.insert(stationEntity);

        final Optional<StationEntity> actual = stationDao.findById(persistStationEntity.getId());

        assertThat(actual).isPresent();
    }

    @Test
    void findById_메소드는_없는_id를_전달하면_빈_Optional을_반환한다() {
        final Optional<StationEntity> actual = stationDao.findById(-999L);

        assertThat(actual).isEmpty();
    }

    @Test
    void existsByName_메소드는_저장되어_있는_이름을_전달하면_true를_반환한다() {
        final StationEntity stationEntity = StationEntity.from("12역");
        final StationEntity persistStationEntity = stationDao.insert(stationEntity);

        final boolean actual = stationDao.existsByName(persistStationEntity.getName());

        assertThat(actual).isTrue();
    }

    @Test
    void existsByName_메소드는_없는_이름을_전달하면_false를_반환한다() {
        final boolean actual = stationDao.existsByName("abc");

        assertThat(actual).isFalse();
    }

    @Test
    void findAllByIds_메소드는_여러_id를_전달하면_해당_id의_station을_반환한다() {
        final StationEntity firstStationEntity = stationDao.insert(StationEntity.from("12역"));
        final StationEntity secondStationEntity = stationDao.insert(StationEntity.from("23역"));
        final Set<Long> ids = Set.of(firstStationEntity.getId(), secondStationEntity.getId());

        final List<StationEntity> actual = stationDao.findAllByIds(ids);

        assertThat(actual).hasSize(2);
    }

    @Test
    void deleteById_메소드는_id를_전달하면_해당_id를_가진_station을_삭제한다() {
        final StationEntity stationEntity = StationEntity.from("12역");
        final StationEntity persistStationEntity = stationDao.insert(stationEntity);

        assertDoesNotThrow(() -> stationDao.deleteById(persistStationEntity.getId()));
    }

}
