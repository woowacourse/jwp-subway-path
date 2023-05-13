package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class StationDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @DisplayName("역을 추가한다.")
    @Test
    void insert() {
        // given
        Station station = new Station("잠실");

        // when
        Station insertedStation = stationDao.insert(station);

        // then
        assertThat(insertedStation.getName()).isEqualTo(station.getName());
    }

    @DisplayName("이름이 같은 역을 넣으면 예외를 던진다.")
    @Test
    void insertDuplicatedName() {
        // given
        Station station = new Station("잠실");
        Station insertedStation = stationDao.insert(station);

        // when, then
        assertThatThrownBy(() -> stationDao.insert(station)).isInstanceOf(DataAccessException.class);
    }

    @DisplayName("전체 조회를 한다.")
    @Test
    void findAll() {
        // given
        Station firstStation = new Station("잠실");
        Station secondStation = new Station("선릉");
        Station thirdStation = new Station("잠실나루");

        Station first = stationDao.insert(firstStation);
        Station second = stationDao.insert(secondStation);
        Station third = stationDao.insert(thirdStation);

        // when, then
        assertThat(stationDao.findAll().containsAll(List.of(first, second, third)));
    }

    @DisplayName("아이디로 조회한다.")
    @Test
    void findById() {
        // given
        Station firstStation = new Station("잠실");
        Station first = stationDao.insert(firstStation);

        // when, then
        assertThat(stationDao.findById(first.getId()).get()).isEqualTo(first);
    }

    @DisplayName("없는 아이디로 조회하면 빈 옵셔널을 반환한다.")
    @Test
    void findByNotExistId() {
        // when, then
        assertThat(stationDao.findById(10L)).isEqualTo(Optional.empty());
    }

    @DisplayName("이름으로 조회한다.")
    @Test
    void findByName() {
        // given
        Station firstStation = new Station("잠실");
        Station first = stationDao.insert(firstStation);

        // when, then
        assertThat(stationDao.findByName(first.getName()).get()).isEqualTo(first);
    }

    @DisplayName("없는 이름으로 조회하면 빈 옵셔널을 반환한다.")
    @Test
    void findByNotExistName() {
        // when, then
        assertThat(stationDao.findByName("hardy")).isEqualTo(Optional.empty());
    }

    @DisplayName("업데이트를 한다.")
    @Test
    void update() {
        // given
        Station station = new Station("잠실");
        Station originStation = stationDao.insert(station);

        // when
        stationDao.update(new Station(originStation.getId(), "잠실나루"));

        // then
        assertThat(stationDao.findById(originStation.getId()).get().getName()).isNotEqualTo(station.getName());
    }

    @DisplayName("삭제를 한다.")
    @Test
    void deleteById() {
        // given
        Station station = new Station("잠실");
        Station originStation = stationDao.insert(station);

        // when
        stationDao.deleteById(originStation.getId());

        // then
        assertThat(stationDao.findAll()).isEmpty();
    }
}
