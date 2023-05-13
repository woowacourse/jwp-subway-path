package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import javax.sql.DataSource;
import java.util.List;

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
        assertThatThrownBy(() -> stationDao.insert(station)).isInstanceOf(InvalidDataException.class);
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
        assertThat(stationDao.findById(first.getId())).isEqualTo(first);
    }

    @DisplayName("없는 아이디로 조회하면 예외를 던진다.")
    @Test
    void findByNotExistId() {
        // when, then
        assertThatThrownBy(() -> stationDao.findById(10L)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("이름으로 조회한다.")
    @Test
    void findByName() {
        // given
        Station firstStation = new Station("잠실");
        Station first = stationDao.insert(firstStation);

        // when, then
        assertThat(stationDao.findByName(first.getName())).isEqualTo(first);
    }

    @DisplayName("없는 이름으로 조회하면 예외를 던진다.")
    @Test
    void findByNotExistName() {
        // when, then
        assertThatThrownBy(() -> stationDao.findByName("hardy")).isInstanceOf(NotFoundException.class);
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
        assertThat(stationDao.findById(originStation.getId()).getName()).isNotEqualTo(station.getName());
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
