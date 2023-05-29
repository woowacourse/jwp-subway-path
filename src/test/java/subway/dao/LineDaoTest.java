package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.persistence.dao.LineDao;
import subway.persistence.entity.LineEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/testSchema.sql")
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void 이름과_색으로_호선을_등록한다() {
        ///given
        final LineEntity lineEntity = new LineEntity("2호선", "green");

        ///when
        final LineEntity newLineEntity = lineDao.insert(lineEntity);

        ///then
        assertThat(newLineEntity.getId()).isSameAs(1L);
    }

    @Test
    void 전체_노선을_조회한다() {
        ///given
        final LineEntity line1 = new LineEntity("1호선", "blue");
        final LineEntity line2 = new LineEntity("2호선", "green");
        final LineEntity line3 = new LineEntity("3호선", "orange");
        lineDao.insert(line1);
        lineDao.insert(line2);
        lineDao.insert(line3);

        ///when
        final List<LineEntity> allLines = lineDao.findAll();

        ///then
        assertThat(allLines.size()).isSameAs(3);
    }

    @Test
    void 식별자를_통해_특정노선을_조회한다() {
        ///given
        lineDao.insert(new LineEntity("1호선", "blue"));

        ///when
        final LineEntity line = lineDao.findById(1L).orElseThrow();

        ///then
        assertThat(line.getId()).isSameAs(1L);
    }

    @Test
    void 노선_정보를_수정한다() {
        ///given
        lineDao.insert(new LineEntity("구1호선", "darkblue"));
        LineEntity lineEntity = new LineEntity(1L, "새1호선", "blue");

        ///when
        lineDao.update(lineEntity);
        final LineEntity updated = lineDao.findById(1L).orElseThrow();

        ///then
        assertThat(updated.getId()).isSameAs(1L);
        assertThat(updated.getName()).isEqualTo("새1호선");
        assertThat(updated.getColor()).isEqualTo("blue");
    }
}