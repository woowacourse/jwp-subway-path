package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;
import subway.entity.LineEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.LINE_2;
import static subway.fixture.LineFixture.LINE_999;

@JdbcTest
class LineH2DaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineH2Dao(jdbcTemplate);
    }

    @Test
    void saveTest() {
        final LineEntity savedEntity = saveLine(LINE_999);

        assertThat(savedEntity.getId()).isGreaterThanOrEqualTo(1L);
    }

    @Test
    void findAllTest() {
        final LineEntity savedLine999 = saveLine(LINE_999);
        final LineEntity savedLine2 = saveLine(LINE_2);

        assertThat(lineDao.findAll()).contains(savedLine999, savedLine2);
    }

    @Test
    void findByIdTest() {
        final LineEntity savedLine999 = saveLine(LINE_999);

        final LineEntity foundLine = (LineEntity) lineDao.findById(savedLine999.getId()).get();

        assertThat(foundLine).isEqualTo(savedLine999);
    }

    private LineEntity saveLine(Line line) {
        final LineEntity lineEntity = new LineEntity(line.getName(), line.getColor());
        return lineDao.insert(lineEntity);
    }
}
