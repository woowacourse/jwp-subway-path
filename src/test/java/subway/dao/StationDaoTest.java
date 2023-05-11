package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.station.Station;

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
    @DisplayName("식별자로 역을 조회할 수 있다.")
    void findById_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 저장된_잠실역 = stationDao.insert(잠실역);

        final Long 저장된_잠실역_아이디 = 저장된_잠실역.getId();

        // when
        final Optional<Station> station = stationDao.findById(저장된_잠실역_아이디);

        // then
        assertAll(
                () -> assertThat(station).isNotNull(),
                () -> assertThat(station.get().getName()).isEqualTo("잠실역")
        );
    }

}