package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;
import subway.entity.StationEntity;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("Station 삽입 테스트")
    void insert() {
        // given
        final String name = "반월당역";
        // when
        final Long id = stationDao.insert(StationEntity.from(new Station(name)));
        // then
        assertThat(stationDao.findById(id)).isNotNull();
    }

    @Test
    @DisplayName("전체 Station 조회 테스트")
    void findAll() {
        // given
        final Long id1 = stationDao.insert(StationEntity.from(new Station("반월당역")));
        final Long id2 = stationDao.insert(StationEntity.from(new Station("청라언덕역")));
        // when
        final List<StationEntity> stationEntities = stationDao.findAll();
        // then
        assertAll(
                () -> assertThat(stationEntities).hasSize(2),
                () -> assertThat(stationEntities)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(
                                StationEntity.of(id1, "반월당역"),
                                StationEntity.of(id2, "청라언덕역")
                        ))
        );
    }

    @Test
    @DisplayName("id로 Station 조회 테스트")
    void findById() {
        // given
        final String name = "반월당역";
        final Long id = stationDao.insert(StationEntity.from(new Station(name)));
        // when
        final StationEntity stationEntity = stationDao.findById(id).orElseThrow();
        // then
        assertThat(stationEntity)
                .usingRecursiveComparison()
                .isEqualTo(StationEntity.of(id, name));
    }

    @Test
    @DisplayName("Station 수정 테스트")
    void update() {
        // given
        final String name = "반월당역";
        final Long id = stationDao.insert(StationEntity.from(new Station(name)));
        // when
        final String updatedName = "청라언덕역";
        stationDao.update(id, StationEntity.from(new Station(updatedName)));
        // then
        assertThat(stationDao.findById(id).orElseThrow())
                .usingRecursiveComparison()
                .isEqualTo(StationEntity.of(id, updatedName));
    }

    @Test
    @DisplayName("Station 삭제 테스트")
    void deleteById() {
        // given
        final String name = "반월당역";
        final Long id = stationDao.insert(StationEntity.from(new Station(name)));
        // when
        stationDao.deleteById(id);
        // then
        assertThat(stationDao.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("Station 존재 여부 테스트")
    void notExistsById() {
        // given
        final String name = "반월당역";
        final Long id = stationDao.insert(StationEntity.from(new Station(name)));
        // when
        final boolean isNotExist = stationDao.notExistsById(id);
        // then
        assertThat(isNotExist).isFalse();
    }
}
