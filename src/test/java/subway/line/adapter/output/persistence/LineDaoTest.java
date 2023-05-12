package subway.line.adapter.output.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.line.adapter.output.persistence.LineDao;
import subway.line.adapter.output.persistence.LineEntity;

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
    void 노선_저장_테스트() {
        final LineEntity lineEntity = new LineEntity("1호선", "빨강");

        final LineEntity insertEntity = lineDao.insert(lineEntity);

        assertThat(insertEntity.getLineId()).isNotNull();
        assertThat(insertEntity.getName()).isEqualTo("1호선");
    }

    @Test
    void 전체_조회_테스트() {
        final LineEntity lineEntity = new LineEntity("1호선", "빨강");
        lineDao.insert(lineEntity);

        final List<LineEntity> all = lineDao.findAll();

        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("1호선");
    }

    @Test
    void 식별자_조회_테스트() {
        final LineEntity lineEntityA = new LineEntity("1호선", "빨강");
        final LineEntity lineEntityB = new LineEntity("2호선", "파랑");

        final LineEntity insertA = lineDao.insert(lineEntityA);
        lineDao.insert(lineEntityB);

        final LineEntity byId = lineDao.findById(insertA.getLineId());

        assertThat(byId.getLineId()).isNotNull();
        assertThat(byId.getName()).isEqualTo("1호선");
    }
}
