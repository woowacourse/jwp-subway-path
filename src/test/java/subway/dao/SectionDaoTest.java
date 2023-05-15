package subway.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;

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
       sectionDao.saveSection(1l, 1, "신림", "봉선");
    }

    @Test
    void findSections() {
        Line keyLine = new Line(1l,"testName1", "testColor1");
        Assertions.assertThat(sectionDao.findSections().get(keyLine)).hasSize(4);
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
