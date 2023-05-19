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
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:initializeTestDb.sql")
class SectionDaoTest {

    private static final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
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

        assertThat(sections).usingRecursiveFieldByFieldElementComparator()
                            .contains(
                                    new SectionStationMapper(1L, 2L, "봉천역", 1L, "서울대입구역", 5),
                                    new SectionStationMapper(2L, 1L, "서울대입구역", 4L, "사당역", 7)
                            );
    }

    @DisplayName("노선의 id를 입력했을 때, 해당 노선의 구간 정보를 SectionEntity 체로 반환한다")
    @Test
    void findByLineId() {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(1L).get();

        assertThat(sectionEntities).usingRecursiveFieldByFieldElementComparator()
                                   .containsExactly(
                                           new SectionEntity(1L, 1L, 2L, 1L, 5),
                                           new SectionEntity(2L, 1L, 1L, 4L, 7)
                                   );

    }

    @DisplayName("노선의 UpStationId를 입력했을 때, 해당 구간 정보를 SectionEntity 객체로 반환한다")
    @Test
    void findByUpStationId() {
        SectionEntity section = sectionDao.findByUpStationId(1L, 1L).get();

        assertThat(section).usingRecursiveComparison()
                           .isEqualTo(new SectionEntity(2L, 1L, 1L, 4L, 7));
    }

    @DisplayName("노선의 DownStationId를 입력했을 때, 해당 구간 정보를 SectionEntity 객체로 반환한다")
    @Test
    void findByDownStationId() {
        SectionEntity section = sectionDao.findByDownStationId(1L, 1L).get();

        assertThat(section).usingRecursiveComparison()
                           .isEqualTo(new SectionEntity(1L, 1L, 2L, 1L, 5));
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
                                 .ignoringFields("id")
                                 .isEqualTo(new SectionEntity(1L, 1L, 3L, 5));
    }

    @DisplayName("특정 노선에 여러 구간을 한 번에 추가한다")
    @Test
    void insertAll() {
        SectionEntity addSection1 = new SectionEntity(1L, 3L, 2L, 6);
        SectionEntity addSection2 = new SectionEntity(1L, 4L, 6L, 7);
        List<SectionEntity> addSections = List.of(addSection1, addSection2);

        sectionDao.insertAll(addSections);

        List<SectionEntity> resultSections = jdbcTemplate.query(
                "SELECT * FROM section WHERE line_id = :line_id",
                new MapSqlParameterSource("line_id", 1L),
                sectionEntityRowMapper
        );

        assertThat(resultSections).usingRecursiveFieldByFieldElementComparator()
                                  .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                                  .containsAll(addSections);
    }

    @DisplayName("특정 노선의 상행선 아이디를 통해 구간을 수정한다")
    @Test
    void updateByUpStationId() {
        SectionEntity updateSection = new SectionEntity(1L, 1L, 2L, 6L, 5);

        sectionDao.updateByUpStationId(updateSection);

        SectionEntity sectionEntities = jdbcTemplate.queryForObject(
                "SELECT * FROM section WHERE line_id = :lineId AND up_station_id = :upStationId",
                new BeanPropertySqlParameterSource(updateSection),
                sectionEntityRowMapper
        );
        assertThat(sectionEntities).usingRecursiveComparison()
                                   .isEqualTo(updateSection);
    }

    @DisplayName("특정 노선의 상행선 아이디를 통해 구간을 수정한다")
    @Test
    void updateByDownStationId() {
        SectionEntity updateSection = new SectionEntity(1L, 1L, 6L, 1L, 5);

        sectionDao.updateByDownStationId(updateSection);

        SectionEntity sectionEntities = jdbcTemplate.queryForObject(
                "SELECT * FROM section WHERE id = :id",
                new BeanPropertySqlParameterSource(updateSection),
                sectionEntityRowMapper
        );
        assertThat(sectionEntities).usingRecursiveComparison()
                                   .isEqualTo(updateSection);
    }

    @DisplayName("특정 노선에 구간을 삭제한다")
    @Test
    void delete() {
        SectionEntity deleteSection = new SectionEntity(1L, 1L, 2L, 1L, 5);

        sectionDao.delete(deleteSection);

        List<SectionEntity> sectionEntities = jdbcTemplate.query(
                "SELECT * FROM section",
                sectionEntityRowMapper
        );
        assertThat(sectionEntities).usingRecursiveFieldByFieldElementComparator()
                                   .doesNotContain(deleteSection);
    }

    @DisplayName("특정 노선에 여러 구간을 한 번에 삭제한다")
    @Test
    void deleteAll() {
        SectionEntity deleteSection1 = new SectionEntity(1L, 1L, 2L, 1L, 5);
        SectionEntity deleteSection2 = new SectionEntity(2L, 1L, 2L, 1L, 5);
        List<SectionEntity> deleteSections = List.of(deleteSection1, deleteSection2);

        sectionDao.deleteAll(deleteSections);

        List<SectionEntity> sectionEntities = jdbcTemplate.query(
                "SELECT * FROM section",
                sectionEntityRowMapper
        );
        assertAll(
                () -> assertThat(sectionEntities).usingRecursiveFieldByFieldElementComparator()
                                                 .doesNotContain(deleteSection1),
                () -> assertThat(sectionEntities).usingRecursiveFieldByFieldElementComparator()
                                                 .doesNotContain(deleteSection2)
        );
    }
}
