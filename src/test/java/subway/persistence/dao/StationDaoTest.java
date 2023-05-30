package subway.persistence.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import subway.persistence.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixtures.station.StationFixture.강남역Entity;
import static subway.fixtures.station.StationFixture.역삼역Entity;

@JdbcTest
public class StationDaoTest {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    private final RowMapper<StationEntity> rowMapper = (resultSet, rowNumber) -> new StationEntity(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );

    @Autowired
    StationDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @DisplayName("DB에 역을 삽입한다.")
    @Test
    void shouldInsertStationWhenRequest() {
        StationEntity stationEntity = new StationEntity("신도림역");
        long id = stationDao.insert(stationEntity);

        String sql = "select * from STATION where id = ?";
        StationEntity actualStationEntity = jdbcTemplate.queryForObject(sql, rowMapper, id);

        assertThat(actualStationEntity.getName()).isEqualTo("신도림역");
    }

    @DisplayName("DB에 있는 모든 역을 가져온다.")
    @Test
    void shouldReturnAllStationsWhenRequest() {
        stationDao.insert(강남역Entity);
        stationDao.insert(역삼역Entity);

        List<StationEntity> stationEntities = stationDao.findAll();

        assertThat(stationEntities.get(0)).usingRecursiveComparison().isEqualTo(강남역Entity);
        assertThat(stationEntities.get(1)).usingRecursiveComparison().isEqualTo(역삼역Entity);
    }

    @DisplayName("ID를 통해 DB에 있는 역을 가져온다.")
    @Test
    void shouldReturnStationWhenInputId() {
        long id = stationDao.insert(new StationEntity("강남역"));

        StationEntity stationEntity = stationDao.findById(id).get();

        assertThat(stationEntity).usingRecursiveComparison().isEqualTo(new StationEntity(id, "강남역"));
    }

    @DisplayName("ID를 통해 DB에 있는 역을 가져온다.")
    @Test
    void shouldUpdateStationWhenInputId() {
        long id = stationDao.insert(new StationEntity("강남역"));
        stationDao.update(new StationEntity(id, "역삼역"));

        StationEntity stationEntity = stationDao.findById(id).get();

        assertThat(stationEntity.getName()).isEqualTo("역삼역");
    }

    @DisplayName("ID를 통해 DB에 있는 역을 삭제한다.")
    @Test
    void shouldDeleteStationWhenInputId() {
        stationDao.insert(강남역Entity);

        stationDao.deleteById(1L);

        assertThat(stationDao.findById(1L)).isEqualTo(Optional.empty());
    }
}
