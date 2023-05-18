package subway.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void Line을_저장한다() {
        // given
        LineEntity line = new LineEntity("2호선");

        // when
        LineEntity saveLine = lineDao.insert(line);

        // then
        assertThat(saveLine.getId()).isPositive();
    }

    @Test
    void Line을_전체_조회한다() {
        // given
        final LineEntity firstLine = new LineEntity("1호선");
        final LineEntity secondLine = new LineEntity("2호선");
        lineDao.insert(firstLine);
        lineDao.insert(secondLine);

        // when
        final List<LineEntity> lines = lineDao.findAll();

        // then
        assertThat(lines).map(LineEntity::getName)
                .containsExactly(firstLine.getName(), secondLine.getName());

    }

    @Test
    void Line을_ID_기준으로_단건_조회한다() {
        // given
        final LineEntity saveLine1 = lineDao.insert(new LineEntity("1호선"));
        final LineEntity saveLine2 = lineDao.insert(new LineEntity("2호선"));

        // when
        final LineEntity findLine1 = lineDao.findById(saveLine1.getId());
        final LineEntity findLine2 = lineDao.findById(saveLine2.getId());

        // then
        assertThat(findLine1).isEqualTo(saveLine1);
        assertThat(findLine2).isEqualTo(saveLine2);
    }
}
