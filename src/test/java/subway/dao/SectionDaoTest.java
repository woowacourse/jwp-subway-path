package subway.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class SectionDaoTest {

    SectionDao sectionDao;
    LineDao lineDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    @DisplayName("1호선에 구간 3개를 등록한다.")
    void save() {

        // given
        lineDao.save(new LineEntity(1L, "1호선"));

        List<SectionEntity> sections = List.of(
                new SectionEntity("A역", "B역", 1, 1L),
                new SectionEntity("B역", "C역", 2, 1L),
                new SectionEntity("C역", "D역", 3, 1L));

        // when
        sectionDao.batchSave(sections);

        //then
        assertThat(sectionDao.findSectionsByLineId(1L)).usingRecursiveComparison().ignoringFields("id").isEqualTo(sections);
    }

    @Test
    @DisplayName("1호선에 존재하는 구간 3개를 모두 삭제한다.")
    void deleteAll() {

        // given
        lineDao.save(new LineEntity(1L, "1호선"));

        List<SectionEntity> sections = List.of(
                new SectionEntity("A역", "B역", 1, 1L),
                new SectionEntity("B역", "C역", 2, 1L),
                new SectionEntity("C역", "D역", 3, 1L));

        // when
        sectionDao.deleteAll(1L);

        assertThat(sectionDao.findSectionsByLineId(1L)).hasSize(0);
    }

    @Test
    @DisplayName("1호선에 속한 모든 섹션(3개)이 반환된다")
    void findSectionsByLineId() {

        // given
        lineDao.save(new LineEntity(1L, "1호선"));

        List<SectionEntity> insertedSections = List.of(
                new SectionEntity("A역", "B역", 1, 1L),
                new SectionEntity("B역", "C역", 2, 1L),
                new SectionEntity("C역", "D역", 3, 1L));

        sectionDao.batchSave(insertedSections);

        // when
        List<SectionEntity> sections = sectionDao.findSectionsByLineId(1L);


        // then
        assertThat(sections).hasSize(3);
    }

    @AfterEach
    void resetTable() {
        jdbcTemplate.update("TRUNCATE TABLE section");
        jdbcTemplate.update("alter table section alter column id restart");

        jdbcTemplate.update("TRUNCATE TABLE line");
        jdbcTemplate.update("alter table line alter column id restart");
    }
}