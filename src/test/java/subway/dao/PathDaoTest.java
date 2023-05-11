package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.PathEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class PathDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PathDao pathDao;

    @BeforeEach
    void setUp() {
        pathDao = new PathDao(jdbcTemplate);
    }

    @DisplayName("경로를 저장한다")
    @Test
    void save() {
        final PathEntity pathEntity = new PathEntity(1L, "하행역", 2L, 10L);
        pathDao.save(pathEntity, 1L);

        String sql = "SELECT * FROM paths WHERE id = ?";
        final PathEntity extracted = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new PathEntity(
                rs.getLong("up_station_id"),
                rs.getLong("down_station_id"),
                rs.getLong("distance")
        ), 1L);

        assertThat(extracted.getDistance()).isEqualTo(10);
        assertThat(extracted.getStationId()).isEqualTo(1L);
        assertThat(extracted.getUpStationId()).isEqualTo(2L);
    }

    @DisplayName("노선 ID를 통해 노선에 포함된 모든 경로를 순서대로 조회한다")
    @Test
    void findPathsByLineId() {
        //given
        final PathEntity pathEntity1 = new PathEntity(1L, "하행역", 2L, 10L);
        final PathEntity pathEntity2 = new PathEntity(2L, "하행역", 3L, 10L);
        final PathEntity pathEntity3 = new PathEntity(3L, "하행역", 4L, 10L);

        pathDao.save(pathEntity1, 1L);
        pathDao.save(pathEntity2, 1L);
        pathDao.save(pathEntity3, 1L);

        //when
        final List<PathEntity> pathsByLineId = pathDao.findPathsByLineId(1L);

        //then
        System.out.println(pathsByLineId.toString());


    }

}