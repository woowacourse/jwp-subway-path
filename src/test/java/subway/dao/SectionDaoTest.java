package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.entity.SectionEntity;
import subway.dao.vo.SectionStationMapper;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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

    @DisplayName("노선의 id를 입력했을 때, 해당 노선의 구간 정보를 SectionStationMapper 객체로 반환한다")
    @Test
    void findSectionsByLineId() {
        List<SectionStationMapper> sections = sectionDao.findSectionsByLineId(1L);
        System.out.println("answer : " + sections);

        assertThat(sections).usingRecursiveFieldByFieldElementComparator()
                            .contains(
                                    new SectionStationMapper(2L, "봉천역", 1L, "서울대입구역", 5),
                                    new SectionStationMapper(1L, "서울대입구역", 4L, "사당역", 7)
                            );
    }

    @DisplayName("노선의 id를 입력했을 때, 해당 노선의 구간 정보를 SectionEntity 체로 반환한다")
    @Test
    void findByLineId() {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(1L)
                                                        .get();
        assertThat(sectionEntities).usingRecursiveFieldByFieldElementComparator()
                                   .containsExactly(
                                           new SectionEntity(1L, 2L, 1L, 5),
                                           new SectionEntity(1L, 1L, 4L, 7)
                                   );

    }

    @DisplayName("노선의 UpStationId를 입력했을 때, 해당 구간 정보를 SectionEntity 객체로 반환한다")
    @Test
    void findByUpStationId() {
        SectionEntity section = sectionDao.findByUpStationId(1L, 1L)
                                          .get();
        assertThat(section).usingRecursiveComparison()
                           .isEqualTo(new SectionEntity(1L, 1L, 4L, 7));
    }

    @DisplayName("노선의 DownStationId를 입력했을 때, 해당 구간 정보를 SectionEntity 객체로 반환한다")
    @Test
    void findByDownStationId() {
        SectionEntity section = sectionDao.findByDownStationId(1L, 1L)
                                          .get();
        assertThat(section).usingRecursiveComparison()
                           .isEqualTo(new SectionEntity(1L, 2L, 1L, 5));
    }

    @DisplayName("특정 노선에 구간을 추가한다")
    @Test
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
                                 .isEqualTo(new SectionEntity(1L, 1L, 3L, 5));
    }

    @DisplayName("특정 노선의 상행선 아이디를 통해 구간을 수정한다")
    @Test
    void updateByUpStationId() {
        SectionEntity updateSection = new SectionEntity(1L, 2L, 6L, 5);
        sectionDao.updateByUpStationId(updateSection);

        List<SectionEntity> sectionEntities = jdbcTemplate.query(
                "SELECT * FROM section WHERE line_id = :lineId AND up_station_id = :upStationId",
                new BeanPropertySqlParameterSource(updateSection),
                sectionEntityRowMapper
        );
    }

    @DisplayName("특정 노선의 상행선 아이디를 통해 구간을 수정한다")
    @Test
    void updateByDownStationId() {
        SectionEntity updateSection = new SectionEntity(1L, 6L, 1L, 5);
        sectionDao.updateByUpStationId(updateSection);

        List<SectionEntity> sectionEntities = jdbcTemplate.query(
                "SELECT * FROM section WHERE line_id = :lineId AND up_station_id = :upStationId",
                new BeanPropertySqlParameterSource(updateSection),
                sectionEntityRowMapper
        );
    }

    @DisplayName("특정 노선에 구간을 추가한다")
    @Test
    void delete() {
        SectionEntity deleteSection = new SectionEntity(1L, 2L, 1L, 5);
        sectionDao.delete(deleteSection);

        List<SectionEntity> sectionEntities = jdbcTemplate.query(
                "SELECT * FROM section",
                sectionEntityRowMapper
        );
        assertThat(sectionEntities).usingRecursiveFieldByFieldElementComparator()
                                   .doesNotContain(deleteSection);
    }
}
