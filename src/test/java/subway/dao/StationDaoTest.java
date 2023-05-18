package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.StationEntity;
import subway.exception.StationNotFoundException;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@DisplayName("Station Dao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StationDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        stationDao = new StationDao(dataSource);
    }

    // insert test
    @Test
    @DisplayName("Station 을 저장한다.")
    void insert() {
        // given
        StationEntity stationEntity = new StationEntity("insert");

        // when
        stationDao.insert(stationEntity);

        // then
        String sql = "SELECT * FROM station WHERE name = ?";
        StationEntity insertStation = jdbcTemplate.queryForObject(
                sql,
                (rs, rn) -> new StationEntity(
                        rs.getLong("id"),
                        rs.getString("name")),
                "insert");
        assertThat(insertStation.getName()).isEqualTo("insert");
    }

    // findById
    @Test
    @DisplayName("Station 을 ID 로 조회한다. (조회 결과가 있는 경우)")
    @Sql("/station_test_data.sql")
    void findById_notEmpty() {
        List<StationEntity> station = stationDao.findById(1L);

        assertThat(station).hasSize(1);
        assertThat(station.get(0).getId()).isEqualTo(1L);
        assertThat(station.get(0).getName()).isEqualTo("잠실");
    }

    @Test
    @DisplayName("Station 을 ID 로 조회한다. (조회 결과가 없는 경우)")
    @Sql("/station_test_data.sql")
    void findById_empty() {
        List<StationEntity> station = stationDao.findById(6L);

        assertThat(station).isEmpty();
    }

    @Test
    @DisplayName("Station 을 Name 으로 조회한다. (조회 결과가 있는 경우)")
    @Sql("/station_test_data.sql")
    void findByName_notEmpty() {
        List<StationEntity> station = stationDao.findByName("잠실");

        assertThat(station).hasSize(1);
        assertThat(station.get(0).getId()).isEqualTo(1L);
        assertThat(station.get(0).getName()).isEqualTo("잠실");
    }

    @Test
    @DisplayName("Station 을 Name 으로 조회한다. (조회 결과가 없는 경우)")
    @Sql("/station_test_data.sql")
    void findByName_empty() {
        List<StationEntity> station = stationDao.findByName("재연");

        assertThat(station).isEmpty();
    }

    /**
     * TRUNCATE TABLE station;
     * ALTER TABLE station auto_increment = 1;
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */

    // findAll
    @Test
    @DisplayName("Station 전부를 조회한다.")
    @Sql("/station_test_data.sql")
    void findAll() {
        List<StationEntity> stations = stationDao.findAll();

        assertThat(stations).hasSize(5)
                .containsAll(List.of(
                new StationEntity(1L, "잠실"),
                new StationEntity(2L, "잠실새내"),
                new StationEntity(3L, "종합운동장"),
                new StationEntity(4L, "석촌"),
                new StationEntity(5L, "송파")
        ));
    }

    // update (0)

    // update (1)

    // delete (0)

    // delete (1)
}
