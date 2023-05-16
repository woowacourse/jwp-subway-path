package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.fixture.EntityFixture.이호선_초록색_Entity;
import static subway.fixture.EntityFixture.일호선_남색_Entity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:test_data.sql")
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    void 호선_정보를_삽입한다() {
        // when
        final LineEntity lineEntity = lineDao.insert(일호선_남색_Entity);

        // then
        assertSoftly(softly -> {
            softly.assertThat(lineEntity.getName()).isEqualTo("일호선");
            softly.assertThat(lineEntity.getColor()).isEqualTo("남색");
        });
    }

    @Test
    void 전체_호선_정보를_조회한다() {
        // given
        lineDao.insert(일호선_남색_Entity);
        lineDao.insert(이호선_초록색_Entity);

        // when
        final List<LineEntity> lineEntities = lineDao.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(lineEntities).hasSize(2);
            final LineEntity firstLine = lineEntities.get(0);
            final LineEntity secondLine = lineEntities.get(1);
            softly.assertThat(firstLine.getName()).isEqualTo("일호선");
            softly.assertThat(firstLine.getColor()).isEqualTo("남색");
            softly.assertThat(secondLine.getName()).isEqualTo("이호선");
            softly.assertThat(secondLine.getColor()).isEqualTo("초록색");
        });
    }

    @Test
    void id로_호선_정보를_조회한다() {
        // given
        final Long id = lineDao.insert(일호선_남색_Entity).getId();

        // when
        final LineEntity lineEntity = lineDao.findById(id);

        // then
        assertSoftly(softly -> {
            softly.assertThat(lineEntity.getName()).isEqualTo("일호선");
            softly.assertThat(lineEntity.getColor()).isEqualTo("남색");
        });
    }

    @Test
    void id로_호선_정보를_조회할_때_호선이_없다면_예외를_던진다() {
        // given
        final Long wrongId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> lineDao.findById(wrongId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 호선입니다.");
    }

    @Test
    void 호선_정보를_수정한다() {
        // given
        final Long id = lineDao.insert(일호선_남색_Entity).getId();

        // when
        lineDao.update(new LineEntity(id, "이호선", "초록색"));

        // then
        assertSoftly(softly -> {
            final LineEntity lineEntity = lineDao.findById(id);
            softly.assertThat(lineEntity.getName()).isEqualTo("이호선");
            softly.assertThat(lineEntity.getColor()).isEqualTo("초록색");
        });
    }

    @Test
    void id로_호선_정보를_삭제한다() {
        // given
        final Long id = lineDao.insert(일호선_남색_Entity).getId();

        // when
        lineDao.deleteById(id);

        // then
        final List<LineEntity> allLines = lineDao.findAll();
        assertThat(allLines).hasSize(0);
    }
}
