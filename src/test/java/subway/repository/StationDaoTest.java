package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.exception.DuplicatedStationNameException;
import subway.exception.StationNotFoundException;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.entity.RowMapperUtil.stationEntityRowMapper;

@JdbcTest
@DisplayName("Station Dao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/station_test_data.sql")
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

    @Test
    @DisplayName("생성 성공")
    void insert_success() {
        // given
        final String name = "잠실나루";
        final StationEntity stationEntity = new StationEntity(name);

        // when
        final StationEntity insertedStationEntity = stationDao.insert(stationEntity);

        // then
        assertThat(insertedStationEntity.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("생성 실패 - 중복된 역이름")
    void insert_fail_duplicated_name() {
        // given
        final String name = "잠실";
        final StationEntity stationEntity = new StationEntity(name);

        // when, then
        assertThatThrownBy(() -> stationDao.insert(stationEntity))
                .isInstanceOf(DuplicatedStationNameException.class);
    }

    @Test
    @DisplayName("전체 조회 성공")
    void findAll_success() {
        // given, when
        final List<StationEntity> stationEntities = stationDao.findAll();

        // then
        assertAll(
                () -> assertThat(stationEntities).hasSize(5),
                () -> assertThat(stationEntities
                        .stream()
                        .map(StationEntity::getName)
                        .collect(Collectors.toUnmodifiableList()))
                        .containsAll(List.of("잠실", "잠실새내", "종합운동장", "석촌", "송파"))
        );
    }

    @Test
    @DisplayName("id로 조회 성공")
    void findById_success() {
        // given
        final long id = 1L;

        // when
        final StationEntity stationEntity = stationDao.findById(id);

        // then
        assertThat(stationEntity.getName()).isEqualTo("잠실");
    }

    @Test
    @DisplayName("id로 조회 실패 - 없는 id")
    void findById_fail_id_not_found() {
        // given
        final long id = 7L;

        // when, then
        assertThatThrownBy(() -> stationDao.findById(id))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("이름으로 조회 성공")
    void findByName_success() {
        // given
        final String name = "잠실";

        // when
        final StationEntity stationEntity = stationDao.findByName(name);

        // then
        assertThat(stationEntity.getName()).isEqualTo("잠실");
    }

    @Test
    @DisplayName("이름으로 조회 실패 - 없는 역")
    void findByName_fail_station_not_found() {
        // given
        final String name = "디투";

        // when, then
        assertThatThrownBy(() -> stationDao.findByName(name))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("역 존재 여부 확인 - 존재 안함")
    void IsNotExist_true() {
        // given
        final long id = 7L;

        // when, then
        assertThat(stationDao.isNotExist(id)).isTrue();
    }

    @Test
    @DisplayName("역 존재 여부 확인 - 존재")
    void IsNotExist_false() {
        // given
        final long id = 1L;

        // when, then
        assertThat(stationDao.isNotExist(id)).isFalse();
    }

    @Test
    @DisplayName("이름으로 id 조회 성공")
    void findIdByName_success() {
        // given
        final String name = "잠실";

        // when
        final Long stationId = stationDao.findIdByName(name);

        // then
        assertThat(stationId).isEqualTo(1L);
    }

    @Test
    @DisplayName("이름으로 id 조회 실패 - 존재하지 않는 이름")
    void findIdByName_fail_name_not_found() {
        // given
        final String name = "포비";

        // when, then
        assertThatThrownBy(() -> stationDao.findIdByName(name))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("수정 성공")
    void update_success() {
        // given
        final long id = 1L;
        final String name = "실잠";
        final StationEntity stationEntity = new StationEntity(id, name);

        // when
        stationDao.update(stationEntity);

        // then
        final String findSql = "SELECT * FROM station WHERE id = ?";
        final StationEntity foundStationEntity = jdbcTemplate.queryForObject(findSql, stationEntityRowMapper, id);
        assertAll(
                () -> assertThat(stationEntity.getId()).isEqualTo(foundStationEntity.getId()),
                () -> assertThat(stationEntity.getName()).isEqualTo(foundStationEntity.getName())
        );
    }

    @Test
    @DisplayName("수정 실패 - 중복되는 이름으로 수정")
    void update_fail_duplicated_station_name() {
        // given
        final long id = 1L;
        final String name = "잠실새내";
        final StationEntity stationEntity = new StationEntity(id, name);

        // when, then
        assertThatThrownBy(() -> stationDao.update(stationEntity))
                .isInstanceOf(DuplicatedStationNameException.class);
    }

    @Test
    @DisplayName("수정 실패 - 존재하지 않는 id")
    void update_fail_id_not_found() {
        // given
        final long id = 7L;
        final String name = "실잠";
        final StationEntity stationEntity = new StationEntity(id, name);

        // when, then
        assertThatThrownBy(() -> stationDao.update(stationEntity))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("삭제 성공")
    void deleteById_success() {
        // given
        final long id = 1L;

        // when
        stationDao.deleteById(id);

        // then
        final String findSql = "SELECT * FROM station WHERE id = ?";
        assertThatThrownBy(() -> jdbcTemplate.queryForObject(findSql, stationEntityRowMapper, id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("삭제 실패 - 존재하지 않는 id")
    void delete_fail_id_not_found() {
        // given
        final long id = 7L;

        // when, then
        assertThatThrownBy(() -> stationDao.deleteById(id))
                .isInstanceOf(StationNotFoundException.class);
    }
}
