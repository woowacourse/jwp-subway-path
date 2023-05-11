package subway.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql(scripts = {"classpath:truncate.sql", "classpath:lineTest.sql", "classpath:sectionTest.sql", "classpath:stationTest.sql"})
class SectionDaoTest {
    private final SectionDao sectionDao;

    @Autowired
    SectionDaoTest(JdbcTemplate jdbcTemplate) {
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    void saveSection() {
       // sectionDao.saveSection(1l, 1, "testDep", "testArr");
    }

    @Test
    void findSections() {
    }

    @Test
    void findSectionsByLineId() {
    }

    @Test
    void findSectionByLineIdAndStationId() {
    }

    @Test
    void deleteSection() {
    }
}
