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
import subway.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SectionDaoTest {

    private static final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rn) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getInt("distance"),
            rs.getLong("previous_station_id"),
            rs.getLong("next_station_id")
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
    @DisplayName("Section 을 저장한다.")
    void save_success() {
        SectionEntity sectionEntity = new SectionEntity(1L, 10, 1L, 5L);

        sectionDao.insert(sectionEntity);

        String sql = "SELECT * FROM section WHERE line_id = ? AND previous_station_id = ? AND next_station_id = ?";
        List<SectionEntity> response = jdbcTemplate.query(sql, sectionEntityRowMapper, 1L ,1L, 5L);
        assertThat(response).hasSize(1)
                .anyMatch(entity -> entity.getLineId() == 1L)
                .anyMatch(entity -> entity.getPreviousStationId() == 1L)
                .anyMatch(entity -> entity.getNextStationId() == 5L)
                .anyMatch(entity -> entity.getDistance() == 10);
    }

    /**
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5)
     */
    @Test
    @DisplayName("line 의 id 로 Section 을 가져온다.")
    @Sql("/section_test_data.sql")
    void findByLineIdAndPreviousStationId_success() {
        List<SectionEntity> result = sectionDao.findByLineId(1L);

        assertThat(result).hasSize(2)
                .anyMatch(sectionEntity -> sectionEntity.getLineId() == 1L
                                && sectionEntity.getDistance() == 3
                                && sectionEntity.getPreviousStationId() == 1L
                                && sectionEntity.getNextStationId() == 2L)
                .anyMatch(sectionEntity -> sectionEntity.getLineId() == 1L
                        && sectionEntity.getDistance() == 3
                        && sectionEntity.getPreviousStationId() == 1L
                        && sectionEntity.getNextStationId() == 2L);
    }

    /**
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     */
    @Test
    @DisplayName("Section을 삭제한다. (성공)")
    @Sql("/section_test_data.sql")
    void delete_success() {
        int removeCount = sectionDao.deleteById(1L);

        String sql = "SELECT * FROM section WHERE id = ?";
        List<SectionEntity> sectionEntity = jdbcTemplate.query(sql, sectionEntityRowMapper, 1L);
        assertThat(removeCount).isEqualTo(1);
        assertThat(sectionEntity).isEmpty();
    }

    /**
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     */
    @Test
    @DisplayName("Section을 삭제한다. (실패)")
    @Sql("/section_test_data.sql")
    void delete_fail() {
        int removeCount = sectionDao.deleteById(5L);

        assertThat(removeCount).isEqualTo(0);
    }

}
