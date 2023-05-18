package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DisplayName("Station Dao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StationDaoTest {

    private static final RowMapper<StationEntity> stationEntityRowMapper = (rs, rn) -> new StationEntity(
            rs.getLong("id"),
            rs.getString("name"));

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        stationDao = new StationDao(dataSource);
    }

    @Test
    @DisplayName("Station 을 저장한다.")
    void insert() {
        StationEntity stationEntity = new StationEntity("insert");

        stationDao.insert(stationEntity);

        String sql = "SELECT * FROM station WHERE name = ?";
        StationEntity insertStation = jdbcTemplate.queryForObject(
                sql,
                stationEntityRowMapper,
                "insert");
        assertThat(insertStation.getName()).isEqualTo("insert");
    }

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

    @Test
    @DisplayName("Station 을 update 한다. (성공)")
    @Sql("/station_test_data.sql")
    void update_success() {
        StationEntity stationEntity = new StationEntity(1L, "안녕");

        int updateCount = stationDao.update(stationEntity);

        StationEntity updateStationEntity = jdbcTemplate.queryForObject("SELECT * FROM station WHERE id = ?", stationEntityRowMapper, 1L);
        assertThat(updateCount).isEqualTo(1);
        assertThat(updateStationEntity.getId()).isEqualTo(1L);
        assertThat(updateStationEntity.getName()).isEqualTo("안녕");
    }

    @Test
    @DisplayName("Station 을 update 한다. (실패)")
    @Sql("/station_test_data.sql")
    void update_fail() {
        StationEntity stationEntity = new StationEntity(6L, "안녕");

        int updateCount = stationDao.update(stationEntity);

        assertThat(updateCount).isEqualTo(0);
    }

    @Test
    @DisplayName("Station 을 delete 한다. (성공)")
    @Sql("/station_test_data.sql")
    void delete_success() {
        int removeCount = stationDao.deleteById(1L);

        List<StationEntity> deleteStationEntity = jdbcTemplate.query("SELECT * FROM station WHERE id = ?", stationEntityRowMapper, 1L);
        assertThat(deleteStationEntity).isEmpty();
        assertThat(removeCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Station 을 delete 한다. (실패)")
    @Sql("/station_test_data.sql")
    void delete_fail() {
        int removeCount = stationDao.deleteById(6L);

        assertThat(removeCount).isEqualTo(0);
    }

}
