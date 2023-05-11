package subway.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.LineInfo;

import javax.sql.DataSource;

@JdbcTest
@Sql(scripts = {"classpath:truncate.sql", "classpath:lineTest.sql"})
class LineDaoTest {
    private final LineDao lineDao;

    @Autowired
    LineDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.lineDao = new LineDao(jdbcTemplate, dataSource);
    }


    @Test
    void insert() {
        //given
        LineInfo input = new LineInfo("testName", "testColor");

        //when
        LineInfo actual = lineDao.insert(input);

        //then
        Assertions.assertThat(actual.getId()).isEqualTo(3l);
    }

    @Test
    void findAll() {

        Assertions.assertThat(lineDao.findAll()).hasSize(2);
    }

    @Test
    void findById() {
        Assertions.assertThat(lineDao.findById(1l).getName()).isEqualTo("testName1");
    }

    @Test
    void update() {
        Assertions.assertThatNoException()
                .isThrownBy(() -> lineDao.update(new LineInfo(1l, "testName1", "testColor1")));
    }

    @Test
    void deleteById() {
        Assertions.assertThatNoException()
                .isThrownBy(() -> lineDao.deleteById(1l));
    }
}
