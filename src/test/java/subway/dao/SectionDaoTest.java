package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;

@JdbcTest
@Sql("classpath:initializeTestDb.sql")
class SectionDaoTest {

    private static final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;
    private SectionDao sectionDao;

    @BeforeEach
    void setup() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @DisplayName("노선의 id를 입력했을 때, 해당 노선의 구간 정보를 section객체로 반환한다")
    @Test
    void findSectionsByLineId() {
        List<Section> sections = sectionDao.findSectionsByLineId(1L);
        assertThat(sections)
                .containsExactly(
                        new Section(new Station(2L, "봉천역"), new Station(1L, "서울대입구역"), new Distance(5)),
                        new Section(new Station(1L, "서울대입구역"), new Station(4L, "사당역"), new Distance(7)));
    }

    @Test
    @DisplayName("특정 노선에 구간을 추가한다")
    void insert() {
        sectionDao.insert(new SectionEntity(1L, 1L, 3L, 5));
        SectionEntity sectionEntity = jdbcTemplate.queryForObject(
                "SELECT * FROM section WHERE line_id = :line_id and up_station_id = :up_station_id and down_station_id = :down_station_id",
                new MapSqlParameterSource()
                        .addValue("line_id", 1L)
                        .addValue("up_station_id", 1L)
                        .addValue("down_station_id", 3L),
                sectionEntityRowMapper
        );
        assertThat(sectionEntity).usingRecursiveComparison()
                .comparingOnlyFields()
                .isEqualTo(new SectionEntity(1L, 1L, 3L, 5));
    }

    @Test
    @DisplayName("특정 노선에 구간을 추가한다")
    void delete() {
        SectionEntity deleteSection = new SectionEntity(1L, 2L, 1L, 5);
        sectionDao.delete(deleteSection);

        List<SectionEntity> sectionEntities = jdbcTemplate.query(
                "SELECT * FROM section",
                sectionEntityRowMapper
        );
        assertThat(sectionEntities).doesNotContain(deleteSection);
    }
}
