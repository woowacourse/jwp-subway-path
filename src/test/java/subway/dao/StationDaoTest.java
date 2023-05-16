package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.StationEntity;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void init() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    void 역을_저장한다() {
        Long id = stationDao.insert(new StationEntity("잠실역")).getId();
        assertThat(id).isNotNull();
    }

    @Test
    void 전체_조회한다() {
        stationDao.insert(new StationEntity("잠실역1"));
        stationDao.insert(new StationEntity("잠실역2"));
        assertThat(stationDao.findAll()).hasSize(2);
    }

    @Test
    void 단건_조회한다() {
        final Long id = stationDao.insert(new StationEntity("잠실역")).getId();
        assertThat(stationDao.findById(id).get().getId()).isEqualTo(id);
    }

    @Test
    void 역을_수정한다() {
        final Long id = stationDao.insert(new StationEntity("잠실역")).getId();
        StationEntity update = new StationEntity(id, "수정역");

        stationDao.update(update);

        assertThat(stationDao.findById(id).get().getName()).isEqualTo("수정역");
    }

    @Test
    void 역을_삭제한다() {
        final Long id = stationDao.insert(new StationEntity("잠실역")).getId();

        stationDao.deleteById(id);

        assertThat(stationDao.findById(id)).isNotPresent();
    }
}
