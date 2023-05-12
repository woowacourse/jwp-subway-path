package subway.persistence.dao;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/reset-table.sql")
@Import({LineDao.class, SectionDao.class, StationDao.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DaoTest {

    @Autowired
    LineDao lineDao;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    StationDao stationDao;
}
