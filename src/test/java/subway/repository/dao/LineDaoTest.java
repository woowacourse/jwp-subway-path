package subway.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.repository.dao.DaoFixtures.LINE_NO_1;
import static subway.repository.dao.DaoFixtures.LINE_NO_2;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
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
    void 노선을_저장한다() {
        // given
        LineEntity line = LINE_NO_2;

        // when
        LineEntity saveLine = lineDao.insert(line);

        // then
        assertThat(saveLine.getId()).isPositive();
    }

    @Test
    void 노선을_전체_조회한다() {
        // given
        final LineEntity firstLine = LINE_NO_1;
        final LineEntity secondLine = LINE_NO_2;
        lineDao.insert(firstLine);
        lineDao.insert(secondLine);

        // when
        final List<LineEntity> lines = lineDao.findAll();

        // then
        assertThat(lines).map(LineEntity::getName)
                .containsExactly(firstLine.getName(), secondLine.getName());

    }

    @Test
    void 노선을_ID_기준으로_단건_조회한다() {
        // given
        final LineEntity saveLine1 = lineDao.insert(LINE_NO_1);
        final LineEntity saveLine2 = lineDao.insert(LINE_NO_2);

        // when
        final LineEntity findLine1 = lineDao.findById(saveLine1.getId());
        final LineEntity findLine2 = lineDao.findById(saveLine2.getId());

        // then
        assertThat(findLine1).isEqualTo(saveLine1);
        assertThat(findLine2).isEqualTo(saveLine2);
    }

    @Test
    void 노선을_ID_기준으로_삭제한다() {
        // given
        final LineEntity saveLine = lineDao.insert(LINE_NO_1);

        // when
        lineDao.deleteById(saveLine.getId());

        // then
        assertThatThrownBy(() -> lineDao.findById(saveLine.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void 특정_이름의_노선이_존재하면_true를_반환한다() {
        // given
        final LineEntity saveLine = lineDao.insert(LINE_NO_1);

        // when
        final boolean exists = lineDao.existsByName(saveLine.getName());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 특정_이름의_노선이_존재하지_않으면_false를_반환한다() {
        // given
        String nonExistLineName = "존재하지 않는 노선 이름";

        // when
        final boolean exists = lineDao.existsByName(nonExistLineName);

        // then
        assertThat(exists).isFalse();
    }
}
