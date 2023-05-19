package subway.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql(scripts = {"classpath:truncate.sql", "classpath:data/lineTest.sql", "classpath:data/sectionTest.sql", "classpath:data/stationTest.sql"})
class SectionDaoTest {
    private final SectionDao sectionDao;

    @Autowired
    SectionDaoTest(JdbcTemplate jdbcTemplate) {
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    void saveSection() {
        sectionDao.saveSection(1l, 1, "신림", "봉천");
    }

    @Test
    void findSections() {
        Assertions.assertThat(sectionDao.findSections()).hasSize(3);
    }

    @Test
    void findAllSections() {

        Assertions.assertThat(sectionDao.findAllSections()).hasSize(11);
    }

    @Test
    void findSectionsByLineId() {
        Assertions.assertThat(sectionDao.findSectionsByLineId(2l)).hasSize(4);
    }

    @Test
    void findSectionByLineIdAndStationId() {
        Assertions.assertThat(sectionDao.findSectionByLineIdAndStationId(2l, 2l).isSizeTwo()).isTrue();
    }

    @Test
    void deleteSection() {
        Assertions.assertThatNoException()
                .isThrownBy(() -> sectionDao.deleteSection(1l));
    }
}
