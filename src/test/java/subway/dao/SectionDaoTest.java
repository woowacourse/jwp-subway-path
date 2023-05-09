package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import subway.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DisplayName("Section Dao")
class SectionDaoTest {

    private final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rn) -> new SectionEntity(
            rs.getLong("id"), rs.getLong("line_id"), rs.getInt("distance"),
            rs.getLong("previous_station_id"), rs.getLong("next_station_id")
    );

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        sectionDao = new SectionDao(dataSource);
    }

    @Test
    @DisplayName("저장 성공")
    void save_success() {
        // given
        SectionEntity sectionEntity = new SectionEntity.Builder()
                .distance(10)
                .lineId(1L)
                .previousStationId(1L)
                .nextStationId(2L)
                .build();

        // when
        sectionDao.insert(sectionEntity);

        // then
        String sql = "SELECT * FROM section WHERE previous_station_id = ? AND next_station_id = ?";
        List<SectionEntity> response = jdbcTemplate.query(sql, sectionEntityRowMapper, 1, 2);
        assertThat(response).hasSize(1)
                .anyMatch(entity -> entity.getId() == 1L)
                .anyMatch(entity -> entity.getDistance() == 10);
    }

}
