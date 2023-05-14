package subway.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.persistence.entity.SectionDetailEntity;
import subway.persistence.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.persistence.entity.RowMapperUtil.sectionEntityRowMapper;

@JdbcTest
@DisplayName("Section Dao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/station_test_data.sql", "/line_test_data.sql", "/section_test_data.sql"})
class SectionDaoTest {

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
        SectionEntity sectionEntity = new SectionEntity(1L, 10, 1L, 5L);

        // when
        sectionDao.insert(sectionEntity);

        // then
        String sql = "SELECT * FROM section WHERE previous_station_id = ? AND next_station_id = ?";
        List<SectionEntity> response = jdbcTemplate.query(sql, sectionEntityRowMapper, 1, 5);
        assertThat(response).hasSize(1)
                .anyMatch(entity -> entity.getLineId() == 1L)
                .anyMatch(entity -> entity.getPreviousStationId() == 1L)
                .anyMatch(entity -> entity.getNextStationId() == 5L)
                .anyMatch(entity -> entity.getDistance() == 10);
    }

    @Test
    @DisplayName("구간 전체 조회 성공")
    void findAll_success() {
        // given, when
        final List<SectionDetailEntity> sectionDetailEntities = sectionDao.findSectionDetail();

        // then
        assertAll(
                () -> assertThat(sectionDetailEntities).hasSize(4),
                () -> assertThat(sectionDetailEntities.get(0).getDistance()).isEqualTo(3),
                () -> assertThat(sectionDetailEntities.get(0).getLineId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntities.get(0).getLineName()).isEqualTo("2호선"),
                () -> assertThat(sectionDetailEntities.get(0).getLineColor()).isEqualTo("bg-green-600"),
                () -> assertThat(sectionDetailEntities.get(0).getPreviousStationId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntities.get(0).getPreviousStationName()).isEqualTo("잠실"),
                () -> assertThat(sectionDetailEntities.get(0).getNextStationId()).isEqualTo(2L),
                () -> assertThat(sectionDetailEntities.get(0).getNextStationName()).isEqualTo("잠실새내")
        );
    }

    @Test
    @DisplayName("노선 id에 해당하는 구간 조회 성공")
    void findSectionDetailByLineName_success() {
        // given
        final long lineId = 1L;

        // when
        final List<SectionDetailEntity> sectionDetailEntities = sectionDao.findSectionDetailByLineId(lineId);

        // then
        assertAll(
                () -> assertThat(sectionDetailEntities).hasSize(2),
                () -> assertThat(sectionDetailEntities.get(0).getDistance()).isEqualTo(3),
                () -> assertThat(sectionDetailEntities.get(0).getLineId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntities.get(0).getLineName()).isEqualTo("2호선"),
                () -> assertThat(sectionDetailEntities.get(0).getLineColor()).isEqualTo("bg-green-600"),
                () -> assertThat(sectionDetailEntities.get(0).getPreviousStationId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntities.get(0).getPreviousStationName()).isEqualTo("잠실"),
                () -> assertThat(sectionDetailEntities.get(0).getNextStationId()).isEqualTo(2L),
                () -> assertThat(sectionDetailEntities.get(0).getNextStationName()).isEqualTo("잠실새내")
        );
    }

    @Test
    @DisplayName("line id 와 previous station id 로 구간 조회 성공")
    @Sql("/section_test_data.sql")
    void findByLineIdAndPreviousStationId_success() {
        // given, when
        final List<SectionEntity> result = sectionDao.findByLineIdAndPreviousStationId(1L, 1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLineId()).isEqualTo(1L);
        assertThat(result.get(0).getPreviousStationId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("구간 삭제 성공")
    @Sql("/section_test_data.sql")
    void delete_success() {
//        // given
//        final String selectSql = "SELECT id FROM section";
//        List<Long> resultBeforeRemove = jdbcTemplate.query(selectSql, (rs, rn) -> rs.getLong("id"));
//        final SectionEntity sectionEntity = new SectionEntit;
//
//        // when
//        sectionDao.delete(sectionEntity);
//
//        // then
//        List<Long> resultAfterRemove = jdbcTemplate.query(selectSql, (rs, rn) -> rs.getLong("id"));
//        assertThat(resultAfterRemove.size()).isEqualTo(resultBeforeRemove.size() - 1);
    }

}
