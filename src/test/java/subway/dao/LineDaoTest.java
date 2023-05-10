package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:schema.sql")
class LineDaoTest {

    private final LineDao lineDao;

    private LineDaoTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    @DisplayName("이름으로 Line을 조회할 수 있다")
    void findByName() {
        // given
        final Line line = new Line("2호선", "green");
        lineDao.insert(line);

        // when
        final Line foundLine = lineDao.findByName(line.getName());

        // then
        assertThat(foundLine.getName()).isEqualTo(line.getName());
    }

    @Test
    void update() {
        // given
        final Line line = new Line("2호선", "green");
        final Line insertedLine = lineDao.insert(line);
        final Line newLine = new Line("1호선", "blue");

        // when
        lineDao.update(insertedLine.getId(), newLine);

        // then
        final Line foundLine = lineDao.findById(insertedLine.getId());
        assertAll(
                () -> assertThat(foundLine.getName()).isEqualTo(newLine.getName()),
                () -> assertThat(foundLine.getColor()).isEqualTo(newLine.getColor())
        );
    }
}
