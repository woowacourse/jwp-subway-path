package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.StationEntity;
import subway.persistence.dao.StationDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/testdata.sql")
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 역을_등록한다() {
        ///given
        final StationEntity station = new StationEntity("충무로");

        ///when
        final StationEntity inserted = stationDao.insert(station);

        ///then
        assertThat(inserted.getId()).isSameAs(1L);
    }

    @Test
    void 포함된_역을_모두_조회한다() {
        ///given
        final StationEntity station1 = new StationEntity("충무로");
        final StationEntity station2 = new StationEntity("동대입구");
        final StationEntity station3 = new StationEntity("약수");
        stationDao.insert(station1);
        stationDao.insert(station2);
        stationDao.insert(station3);

        ///when,then
        assertThat(stationDao.findAll().size()).isEqualTo(3);
    }

    @Test
    void 식별자를_통해_특정역을_조회한다() {
        ///given
        final StationEntity station = new StationEntity("충무로");
        stationDao.insert(station);

        ///when,then
        assertThat(stationDao.findById(1L).getId()).isEqualTo(1L);
    }

    @Test
    void 역의_정보를_수정한다() {
        ///given
        final StationEntity station = new StationEntity("구충무로");
        stationDao.insert(station);
        final StationEntity newStation = new StationEntity(1L, "신충무로");

        ///when
        stationDao.update(newStation);

        ///then
        assertThat(stationDao.findById(1L).getName()).isEqualTo("신충무로");
    }

    @Test
    void 식별자를_통해_역을_삭제한다() {
        ///given
        stationDao.insert(new StationEntity("구충무로"));
        assertThat(stationDao.findById(1L)).isNotNull();

        ///when
        stationDao.deleteById(1L);

        ///then
        assertThatThrownBy(
                () -> stationDao.findById(1L)
        ).isInstanceOf(EmptyResultDataAccessException.class);

    }

}