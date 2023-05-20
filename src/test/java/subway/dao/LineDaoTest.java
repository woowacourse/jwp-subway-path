package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
public class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void 노선_번호가_존재하는지_확인한다() {
        final LineEntity lineEntity = new LineEntity(2L, "2호선", "초록색");
        lineDao.save(lineEntity);

        final boolean lineNumberExist = lineDao.isLineNumberExist(2L);

        assertThat(lineNumberExist).isTrue();
    }

    @Test
    void 노선_ID가_존재하는지_확인한다() {
        final LineEntity lineEntity = new LineEntity(2L, "2호선", "초록색");
        final Long id = lineDao.save(lineEntity);

        final boolean lineIdExist = lineDao.isLineIdExist(id);

        assertThat(lineIdExist).isTrue();
    }

    @Test
    void 노선_정보를_저장한다() {
        final LineEntity lineEntity = new LineEntity(2L, "2호선", "초록색");
        lineDao.save(lineEntity);

        final LineEntity foundLineEntity = lineDao.findByLineNumber(2L);

        assertThat(lineEntity.getName()).isEqualTo(foundLineEntity.getName());
    }

    @Test
    void 노선_번호로_노선을_조회한다() {
        final LineEntity lineEntity = new LineEntity(2L, "2호선", "초록색");
        lineDao.save(lineEntity);

        final LineEntity foundLineEntity = lineDao.findByLineNumber(2L);

        assertThat(lineEntity.getName()).isEqualTo(foundLineEntity.getName());
    }

    @Test
    void 모든_노선을_조회한다() {
        final LineEntity lineEntity1 = new LineEntity(2L, "2호선", "초록색");
        final LineEntity lineEntity2 = new LineEntity(4L, "4호선", "하늘색");
        lineDao.save(lineEntity1);
        lineDao.save(lineEntity2);

        final List<LineEntity> lineEntities = lineDao.findAll();

        assertThat(lineEntities.size()).isEqualTo(2);
    }

    @Test
    void ID로_노선을_삭제한다() {
        final LineEntity lineEntity = new LineEntity(2L, "2호선", "초록색");
        final Long id = lineDao.save(lineEntity);

        lineDao.deleteByLineId(id);

        final List<LineEntity> lineEntities = lineDao.findAll();
        assertThat(lineEntities.isEmpty()).isTrue();
    }
}
