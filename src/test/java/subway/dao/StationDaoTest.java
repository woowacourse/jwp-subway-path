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

    /**
     * TRUNCATE TABLE station;
     * ALTER TABLE station auto_increment = 1;
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */

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


    // findByName
    @Test
    @DisplayName("이름으로 id 조회 성공")
    @Sql("/station_test_data.sql")
    void findIdByName_success() {
        // given
        final String name = "잠실";

        // when
        final StationEntity stationEntity = stationDao.findByName(name);

        // then
        assertThat(stationEntity.getId()).isEqualTo(1L);
    }

    // findByName
    @Test
    @DisplayName("이름으로 id 조회 실패 - 존재하지 않는 이름 입력")
    @Sql("/station_test_data.sql")
    void findIdByName_fail_name_not_found() {
        // given
        final String name = "포비";

        // expected
        assertThatThrownBy(() -> stationDao.findByName(name))
                .isInstanceOf(StationNotFoundException.class);
    }

    // findAll

    // update (0)

    // update (1)

    // delete (0)

    // delete (1)
}
