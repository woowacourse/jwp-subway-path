package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.SectionEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class SectionDaoTest {

    JdbcTemplate jdbcTemplate;
    SectionDao sectionDao;

    @Autowired
    SectionDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @BeforeEach
    void setup() {
        jdbcTemplate.update("INSERT INTO stations (id, name) VALUES (1, \'테스트역1\')");
        jdbcTemplate.update("INSERT INTO stations (id, name) VALUES (2, \'테스트역2\')");
        jdbcTemplate.update("INSERT INTO stations (id, name) VALUES (3, \'테스트역3\')");
        jdbcTemplate.update("INSERT INTO lines (id, name, color) VALUES (1, \'테스트1호선\', \'테스트색빨강\')");
        jdbcTemplate.update("INSERT INTO lines (id, name, color) VALUES (2, \'테스트2호선\', \'테스트색파랑\')");
    }

    @DisplayName("저장한다")
    @Test
    void 저장한다() {
        //given
        SectionEntity sectionEntity = new SectionEntity(1L, 1L, 2L, 1L, 10);
        //when
        Long save = sectionDao.save(sectionEntity);
        //then
        Long excepted = jdbcTemplate.queryForObject("SELECT id FROM sections WHERE up_station_id = 1 AND down_station_id = 2 AND line_id = 1 AND distance = 10", Long.class);
        assertThat(save).isEqualTo(excepted);
    }

    @DisplayName("모든 구간을 찾는다")
    @Test
    void 모든_역을_찾는다() {
        //given
        jdbcTemplate.update("INSERT INTO sections (id, up_station_id, down_station_id, line_id, distance) VALUES (1, 1, 2, 1, 10)");
        jdbcTemplate.update("INSERT INTO sections (id, up_station_id, down_station_id, line_id, distance) VALUES (2, 2, 3, 2, 10)");
        //when
        List<SectionEntity> all = sectionDao.findAll();
        //then
        assertThat(all).hasSize(2);
    }

    @DisplayName("라인의 구간을 삭제한다")
    @Test
    void 라인의_구간을_삭제한다() {
        //given
        jdbcTemplate.update("INSERT INTO sections (id, up_station_id, down_station_id, line_id, distance) VALUES (1, 1, 2, 1, 10)");
        jdbcTemplate.update("INSERT INTO sections (id, up_station_id, down_station_id, line_id, distance) VALUES (2, 2, 3, 2, 10)");
        //when
        sectionDao.deleteByLineId(1L);
        //then
        List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM sections WHERE line_id = 1", Long.class);
        assertThat(ids).isEmpty();
    }

    @DisplayName("여러 구간을 저장한다")
    @Test
    void 여러_구간을_저장한다() {
        //given
        List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(null, 1L, 2L, 1L, 10),
                new SectionEntity(null, 2L, 3L, 1L, 10)
        );
        //when
        sectionDao.save(sectionEntities);
        //then
        List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM sections WHERE line_id = 1", Long.class);
        assertThat(ids).hasSize(2);
    }

    @DisplayName("여러 구간을 삭제한다")
    @Test
    void 여러_구간을_삭제한다() {
        //given
        jdbcTemplate.update("INSERT INTO sections (id, up_station_id, down_station_id, line_id, distance) VALUES (1, 1, 2, 1, 10)");
        jdbcTemplate.update("INSERT INTO sections (id, up_station_id, down_station_id, line_id, distance) VALUES (2, 2, 3, 2, 10)");
        List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(1L, 1L, 2L, 1L, 10),
                new SectionEntity(2L, 2L, 3L, 2L, 10)
        );
        //when
        sectionDao.delete(sectionEntities);
        //then
        List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM sections", Long.class);
        assertThat(ids).isEmpty();
    }
}
