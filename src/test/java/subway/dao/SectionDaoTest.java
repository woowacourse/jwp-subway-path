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
import static subway.TestFeature.*;

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

    @DisplayName("전체 구간 정보를 SectionStationMapper 객체로 반환한다")
    @Test
    void findAll() {
        // when
        List<SectionStationMapper> sections = sectionDao.findAll();

        // then
        assertThat(sections).usingRecursiveFieldByFieldElementComparator()
                            .contains(
                                    SECTION_STATION_MAPPER_봉천_서울대입구,
                                    SECTION_STATION_MAPPER_서울대입구_사당,
                                    SECTION_STATION_MAPPER_방배_봉천,
                                    SECTION_STATION_MAPPER_인천_방배,
                                    SECTION_STATION_MAPPER_사당_동인천,
                                    SECTION_STATION_MAPPER_사당_낙성
                            );
    }

    @DisplayName("노선의 id를 입력했을 때, 해당 노선의 구간 정보를 SectionStationMapper 객체로 반환한다")
    @Test
    void findSectionsByLineId() {
        // when
        List<SectionStationMapper> sections = sectionDao.findSectionsByLineId(1L);

        // then
        assertThat(sections).usingRecursiveFieldByFieldElementComparator()
                            .contains(
                                    SECTION_STATION_MAPPER_봉천_서울대입구,
                                    SECTION_STATION_MAPPER_서울대입구_사당,
                                    SECTION_STATION_MAPPER_방배_봉천,
                                    SECTION_STATION_MAPPER_인천_방배,
                                    SECTION_STATION_MAPPER_사당_동인천
                            );
    }

    @DisplayName("노선의 id를 입력했을 때, 해당 노선의 구간 정보를 SectionEntity 체로 반환한다")
    @Test
    void findByLineId() {
        // when
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(1L)
                                                        .get();

        // then
        assertThat(sectionEntities).usingRecursiveFieldByFieldElementComparator()
                                   .containsExactly(
                                           SECTION_ENTITY_봉천_서울대입구,
                                           SECTION_ENTITY_서울대입구_사당,
                                           SECTION_ENTITY_방배_봉천,
                                           SECTION_ENTITY_인천_방배,
                                           SECTION_ENTITY_사당_동인천
                                   );

    }

    @DisplayName("노선의 UpStationId를 입력했을 때, 해당 구간 정보를 SectionEntity 객체로 반환한다")
    @Test
    void findByUpStationId() {
        // when
        SectionEntity section = sectionDao.findByUpStationId(1L, 1L)
                                          .get();

        // then
        assertThat(section).usingRecursiveComparison()
                           .isEqualTo(SECTION_ENTITY_서울대입구_사당);
    }

    @DisplayName("노선의 DownStationId를 입력했을 때, 해당 구간 정보를 SectionEntity 객체로 반환한다")
    @Test
    void findByDownStationId() {
        // when
        SectionEntity section = sectionDao.findByDownStationId(1L, 1L)
                                          .get();

        // then
        assertThat(section).usingRecursiveComparison()
                           .isEqualTo(SECTION_ENTITY_봉천_서울대입구);
    }

    @DisplayName("특정 노선에 구간을 추가한다")
    @Test
    void insert() {
        // when
        sectionDao.insert(new SectionEntity(1L, 1L, 3L, 5));

        // then
        List<SectionEntity> sectionEntities = jdbcTemplate.query(
                "SELECT * FROM section WHERE line_id = :line_id",
                new MapSqlParameterSource()
                        .addValue("line_id", 1L),
                sectionEntityRowMapper
        );
        assertThat(sectionEntities).usingRecursiveFieldByFieldElementComparator()
                                 .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                                 .contains(new SectionEntity(1L, 1L, 3L, 5));
    }

    @DisplayName("특정 노선에 여러 구간을 한 번에 추가한다")
    @Test
    void insertAll() {
        // given
        SectionEntity addSection1 = new SectionEntity(1L, 3L, 2L, 6);
        SectionEntity addSection2 = new SectionEntity(1L, 4L, 6L, 7);
        List<SectionEntity> addSections = List.of(addSection1, addSection2);

        // when
        sectionDao.insertAll(addSections);

        // then
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
        // given
        SectionEntity updateSection = new SectionEntity(1L, 1L, 2L, 6L, 5);

        // when
        sectionDao.updateByUpStationId(updateSection);

        // then
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
        // given
        SectionEntity updateSection = new SectionEntity(1L, 1L, 6L, 1L, 5);

        // when
        sectionDao.updateByDownStationId(updateSection);

        // then
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
        // given
        SectionEntity deleteSection = SECTION_ENTITY_봉천_서울대입구;

        // when
        sectionDao.delete(deleteSection);

        // then
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
        // given
        SectionEntity deleteSection1 = SECTION_ENTITY_봉천_서울대입구;
        SectionEntity deleteSection2 = SECTION_ENTITY_서울대입구_사당;
        List<SectionEntity> deleteSections = List.of(deleteSection1, deleteSection2);

        // when
        sectionDao.deleteAll(deleteSections);

        // then
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
