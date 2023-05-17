package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.LineEntity;

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
    void 이름과_색상을_저장한다() {
        // when
        final Long id = lineDao.insert("8호선", "분홍색");

        // then
        final Optional<LineEntity> lineEntity = lineDao.findById(id);
        assertAll(
                () -> assertThat(lineEntity).isPresent(),
                () -> assertThat(lineEntity.get().getName()).isEqualTo("8호선"),
                () -> assertThat(lineEntity.get().getColor()).isEqualTo("분홍색")
        );
    }

    @Test
    void 식별자로_역을_찾을_수_있다() {
        // given
        final Long id = lineDao.insert("8호선", "분홍색");

        // when
        final Optional<LineEntity> lineEntity = lineDao.findById(id);

        // then
        assertThat(lineEntity).isPresent();
    }

    @Test
    void 이름으로_역을_찾을_수_있다() {
        // given
        final String name = "8호선";
        final Long id = lineDao.insert(name, "분홍색");

        // when
        final Optional<LineEntity> lineEntity = lineDao.findByName(name);

        // then
        assertThat(lineEntity).isPresent();
    }

    @Test
    void 존재하는_모든_역을_찾을_수_있다() {
        // given
        lineDao.insert("8호선", "분홍색");
        lineDao.insert("2호선", "초록색");
        lineDao.insert("분당선", "노란색");

        // when
        final List<LineEntity> lineEntities = lineDao.findAll();

        // then
        assertThat(lineEntities).hasSize(3);
    }
}
