package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.entity.SectionEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("classpath:test-schema.sql")
class SectionDaoTest {

    JdbcTemplate jdbcTemplate;
    SectionDao sectionDao;

    @Autowired
    SectionDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @DisplayName("저장한다")
    @Test
    void 저장한다() {
        //given
        SectionEntity sectionEntity = new SectionEntity(null, 5L, 6L, 2L, 10);
        //when
        Long save = sectionDao.save(sectionEntity);
        //then
        Long excepted = jdbcTemplate.queryForObject("SELECT id FROM section WHERE up_station_id = 5 AND down_station_id = 6 AND line_id = 2", Long.class);
        assertThat(save).isEqualTo(excepted);
    }

    @DisplayName("모든 구간을 찾는다")
    @Test
    void 모든_구간을_찾는다() {
        //given
        //when
        List<SectionEntity> all = sectionDao.findAll();
        //then
        assertThat(all).hasSize(4);
    }

    @DisplayName("라인의 구간을 삭제한다")
    @Test
    void 라인의_구간을_삭제한다() {
        //when
        sectionDao.deleteByLineId(1L);
        //then
        List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM section WHERE line_id = 1", Long.class);
        assertThat(ids).isEmpty();
    }

    @DisplayName("여러 구간을 저장한다")
    @Test
    void 여러_구간을_저장한다() {
        //given
        List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(null, 5L, 6L, 2L, 10)
        );
        //when
        sectionDao.save(sectionEntities);
        //then
        List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM section WHERE up_station_id = 5 AND down_station_id = 6 AND line_id = 2", Long.class);
        assertThat(ids).hasSize(1);
    }

    @DisplayName("여러 구간을 삭제한다")
    @Test
    void 여러_구간을_삭제한다() {
        List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(3L, 4L, 3L, 2L, 10),
                new SectionEntity(4L, 3L, 5L, 2L, 15)
        );
        //when
        sectionDao.delete(sectionEntities);
        //then
        List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM section WHERE line_id = 2 ", Long.class);
        assertThat(ids).isEmpty();
    }
}
