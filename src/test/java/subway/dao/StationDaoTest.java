package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.StationEntity;

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
    void 역_id를_입력받아_해당하는_Entity를_반환한다() {
        // given
        Long id = stationDao.save(new StationEntity("잠실역"));

        // when
        StationEntity 역 = stationDao.findById(id).get();

        // expected
        assertThat(역.getId()).isEqualTo(id);
    }

    @Test
    void StationEntity를_입력받아_저장한다() {
        // given
        Long id = stationDao.save(new StationEntity("잠실역"));

        // when
        Long newId = stationDao.save(new StationEntity("잠실새내역"));

        // expected
        assertThat(newId).isEqualTo(id + 1);
    }

    @Test
    void 역_이름을_입력받아_해당하는_Entity를_반환한다() {
        // given
        Long id = stationDao.save(new StationEntity("잠실역"));

        // when
        StationEntity 역 = stationDao.findByName("잠실역").get();

        // expected
        assertThat(역.getId()).isEqualTo(id);
    }

    @Test
    void 역_이름을_입력받아_일치하는_역을_삭제한다() {
        // given
        stationDao.save(new StationEntity("잠실역"));

        // when
        int 삭제된_행_개수 = stationDao.deleteByName("잠실역");

        // expected
        assertThat(삭제된_행_개수).isEqualTo(1);
    }
}
