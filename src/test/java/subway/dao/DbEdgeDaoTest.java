package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import subway.entity.EdgeEntity;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static subway.fixture.LineFixture.LINE_999;
import static subway.fixture.StationFixture.EXPRESS_BUS_TERMINAL_STATION;
import static subway.fixture.StationFixture.SAPYEONG_STATION;

@JdbcTest
class DbEdgeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;
    private DbEdgeDao dbEdgeDao;
    private DbLineDao dbLineDao;
    private DbStationDao dbStationDao;

    @BeforeEach
    void setUp() {
        dbEdgeDao = new DbEdgeDao(jdbcTemplate, dataSource);
        dbStationDao = new DbStationDao(jdbcTemplate, dataSource);
        dbLineDao = new DbLineDao(jdbcTemplate, dataSource);
    }

    @Test
    void save() {
        Long lineId = dbLineDao.saveLine(new LineEntity("1호선")).getId();
        Long stationId = dbStationDao.saveStation(new StationEntity("테스트역")).getId();
        EdgeEntity edgeEntity = new EdgeEntity(lineId, stationId, 0, 3);

        Long id = dbEdgeDao.save(edgeEntity);

        EdgeEntity result = jdbcTemplate.queryForObject("select * from edge where id = ?", (rs, rowNum) ->
                new EdgeEntity(
                        rs.getLong("id"),
                        rs.getLong("line_id"),
                        rs.getLong("station_id"),
                        rs.getInt("station_order"),
                        rs.getInt("distance_to_next")
                ), id);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(edgeEntity);
    }

    @Test
    void deleteAllStationsOfLine() {
        Long lineId = dbLineDao.saveLine(new LineEntity("1호선")).getId();
        Long id1 = dbStationDao.saveStation(new StationEntity("테스트역1")).getId();
        Long id2 = dbStationDao.saveStation(new StationEntity("테스트역2")).getId();
        EdgeEntity edgeEntity1 = new EdgeEntity(lineId, id1, 0, 3);
        EdgeEntity edgeEntity2 = new EdgeEntity(lineId, id2, 1, 3);

        dbEdgeDao.save(edgeEntity1);
        dbEdgeDao.save(edgeEntity2);

        dbEdgeDao.deleteAllEdgesOf(lineId);

        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from edge where line_id = ?", Integer.class, lineId);

        assertThat(count).isEqualTo(0);
    }
}
