package subway.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.vo.Line;

import javax.sql.DataSource;

@JdbcTest
@Sql(scripts = {"classpath:truncate.sql", "classpath:data/lineTest.sql"})
class LineDaoTest {
    private final LineDao lineDao;

    @Autowired
    LineDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.lineDao = new LineDao(jdbcTemplate, dataSource);
    }


    @Test
    void insert() {
        //given
        Line input = new Line("testName", "testColor");

        //when
        Line actual = lineDao.insert(input);

        //then
        Assertions.assertThat(actual.getId()).isEqualTo(4l);
    }

    @Test
    void findAll() {

        Assertions.assertThat(lineDao.findAll()).hasSize(3);
    }

    @Test
    void findById() {
        Assertions.assertThat(lineDao.findById(1l).getName()).isEqualTo("1호선");
    }

    @Test
    void update() {
        Assertions.assertThatNoException()
                .isThrownBy(() -> lineDao.update(new Line(1l, "testName1", "testColor1")));
    }

    @Test
    void deleteById() {
        Assertions.assertThatNoException()
                .isThrownBy(() -> lineDao.deleteById(1l));
    }
}
