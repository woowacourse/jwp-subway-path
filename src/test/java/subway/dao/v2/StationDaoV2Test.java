package subway.dao.v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;

import static org.assertj.core.api.Assertions.assertThat;

class StationDaoV2Test extends DaoTestConfig {

    StationDaoV2 stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDaoV2(jdbcTemplate);
    }

    @Test
    void 역_저장() {
        // when
        final Long 역_식별자값 = stationDao.insert("잠실");

        // expect
        assertThat(역_식별자값)
                .isNotNull()
                .isNotZero();
    }
}
