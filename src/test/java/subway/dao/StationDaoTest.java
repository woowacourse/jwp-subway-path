package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.entity.StationEntity;
import subway.exception.DuplicatedException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.TestFeature.*;

@JdbcTest
@Sql("classpath:initializeTestDb.sql")
class StationDaoTest {

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    private final List<StationEntity> expectStations = List.of(
            STATION_ENTITY_서울대입구,
            STATION_ENTITY_봉천역,
            STATION_ENTITY_낙성대역,
            STATION_ENTITY_사당역,
            STATION_ENTITY_방배역,
            STATION_ENTITY_교대역,
            STATION_ENTITY_인천역,
            STATION_ENTITY_동인천역
    );

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;
    private StationDao stationDao;

    @BeforeEach
    void setup() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @DisplayName("역을 추가한다")
    @Test
    void insert() {
        // when
        StationEntity insertStation = stationDao.insert(new StationEntity("노원역"));

        // then
        StationEntity station = jdbcTemplate.queryForObject(
                "select * from station WHERE id = :id",
                new MapSqlParameterSource("id", insertStation.getId()),
                rowMapper
        );
        assertThat(station).usingRecursiveComparison()
                           .isEqualTo(insertStation);
    }

    @DisplayName("이미 존재하는 역을 추가할 시 예외가 발생한다")
    @Test
    void insertException() {
        // then
        assertThatThrownBy(() ->
                stationDao.insert(new StationEntity("봉천역"))
        ).isInstanceOf(DuplicatedException.class);
    }

    @DisplayName("모든 역을 조회한다")
    @Test
    void findAll() {
        // when
        List<StationEntity> stationEntities = stationDao.findAll();

        // then
        assertThat(stationEntities).usingRecursiveFieldByFieldElementComparator()
                                   .containsAll(expectStations);
    }

    @DisplayName("특정 아이디로 역을 조회한다")
    @Test
    void findById() {
        // when
        Optional<StationEntity> findStation = stationDao.findById(1L);

        // then
        assertAll(
                () -> assertThat(findStation).isPresent(),
                () -> assertThat(findStation.get()).usingRecursiveComparison()
                                                   .isEqualTo(expectStations.get(0))
        );
    }

    @DisplayName("존재하지 않는 아이디로 역을 조회하면 빈 값 반환한다")
    @Test
    void findByIdNotExist() {
        // given
        Long notExistId = 1000L;

        // when
        Optional<StationEntity> findStation = stationDao.findById(notExistId);

        // then
        assertThat(findStation).isEmpty();
    }

    @DisplayName("이름으로 특정 역을 조회할 수 있다")
    @Test
    void findByName() {
        // when
        Optional<StationEntity> findStation = stationDao.findByName("서울대입구역");

        // then
        assertAll(
                () -> assertThat(findStation).isPresent(),
                () -> assertThat(findStation.get()).usingRecursiveComparison()
                                                   .isEqualTo(expectStations.get(0))
        );
    }

    @DisplayName("존재하지 않는 이름으로 역 조회 시 빈 값을 반환한다")
    @Test
    void findByNameNotExist() {
        // given
        String notExistName = "notExist";

        // when
        Optional<StationEntity> findStation = stationDao.findByName(notExistName);

        // then
        assertThat(findStation).isEmpty();
    }

    @DisplayName("특정 역 정보를 수정할 수 있다")
    @Test
    void update() {
        // given
        StationEntity updateStation = new StationEntity(1L, "노원역");

        // when
        stationDao.update(updateStation);

        // then
        StationEntity stationEntity = jdbcTemplate.queryForObject(
                "select * from station WHERE id = :id",
                new BeanPropertySqlParameterSource(updateStation),
                rowMapper
        );
        assertThat(stationEntity).usingRecursiveComparison()
                                 .isEqualTo(updateStation);
    }

    @DisplayName("특정 노선의 정보 수정 시 이미 존재하는 역 정보를 수정하면 예외가 발생한다")
    @Test
    void updateException() {
        // when
        StationEntity updateStation = new StationEntity(1L, "봉천역");

        // then
        assertThatThrownBy(() ->
                stationDao.update(updateStation)
        ).isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("아이디로 특정 역을 삭제할 수 있다")
    @Test
    void deleteById() {
        // given
        StationEntity deleteStation = jdbcTemplate.queryForObject(
                "SELECT * FROM station WHERE id = :id",
                new MapSqlParameterSource("id", 1L),
                rowMapper
        );

        // when
        stationDao.deleteById(deleteStation.getId());

        // then
        List<StationEntity> stationEntities = jdbcTemplate.query(
                "SELECT * FROM station",
                rowMapper
        );
        assertThat(stationEntities).usingRecursiveFieldByFieldElementComparator()
                                   .doesNotContain(deleteStation);
    }

    @DisplayName("아이디가 존재한다면 true를 반환한다")
    @Test
    void isExistId() {
        // given
        long searchId = 1L;

        // when
        boolean existName = stationDao.isExistId(searchId);

        // then
        assertThat(existName).isTrue();
    }

    @DisplayName("아이디가 존재하지 않는다면 false를 반환한다")
    @Test
    void isNotExistId() {
        // given
        long searchId = 100L;

        // when
        boolean existName = stationDao.isExistId(searchId);

        // then
        assertThat(existName).isFalse();
    }

    @DisplayName("이름이 이미 존재한다면 true를 반환한다")
    @Test
    void isExistName() {
        // given
        String searchName = "서울대입구역";

        // when
        boolean existName = stationDao.isExistName(searchName);

        // then
        assertThat(existName).isTrue();
    }

    @DisplayName("이름이 아직 존재하지 않는다면 false를 반환한다")
    @Test
    void isNotExistName() {
        // given
        String searchName = "없는역";

        // when
        boolean existName = stationDao.isExistName(searchName);

        // then
        assertThat(existName).isFalse();
    }
}
