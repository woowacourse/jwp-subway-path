package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        this.stationDao = new StationDao(jdbcTemplate);
    }

    @DisplayName("역 저장 테스트")
    @Test
    void save() {
        //given
        final Station station = new Station("서면역");

        //when
        stationDao.insert(station);

        //then
        assertThat(countRowsInTable(jdbcTemplate, "station")).isOne();
    }

    @DisplayName("이름이 중복된 역을 저장하면 예외가 발생한다")
    @Test
    void save_fail() {
        //given
        final Station station = new Station("서면역");
        stationDao.insert(station);

        //when, then
        assertThatThrownBy(() -> stationDao.insert(station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 이름은 중복될 수 없습니다.");
    }

    @DisplayName("역 조회 테스트")
    @Test
    void findById() {
        //given
        final Station station = new Station("서면역");
        final Station persisted = stationDao.insert(station);

        //when, then
        assertThat(stationDao.findById(persisted.getId()).equals(persisted));
    }

    @DisplayName("존재하지 않는 역을 조회하면 예외가 발생한다")
    @Test
    void findById_fail() {
        //given
        final Station station = new Station("서면역");
        stationDao.insert(station);

        //when, then
        assertThatThrownBy(() -> stationDao.findById(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }
}
