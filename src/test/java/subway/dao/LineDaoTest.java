package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.fixture.LineFixture.Line1;
import subway.fixture.LineFixture.Line2;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class LineDaoTest {

    private LineDao lineDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void 호선을_생성한다() {
        // given
        final LineEntity line = Line1.entity;

        // when
        final Long id = lineDao.insert(line);

        // then
        assertThat(id).isPositive();
    }

    @Test
    void 모든_호선을_조회한다() {
        // given
        final LineEntity line1 = Line1.entity;
        final LineEntity line2 = Line2.entity;
        lineDao.insert(line1);
        lineDao.insert(line2);

        // when
        final List<LineEntity> results = lineDao.findAll();

        // then
        assertAll(
                () -> assertThat(results).extracting(LineEntity::getName)
                        .contains("1호선", "2호선"),
                () -> assertThat(results).extracting(LineEntity::getColor)
                        .contains("파랑", "초록")
        );
    }

    @Test
    void 호선을_ID로_조회한다() {
        // given
        final Long id = lineDao.insert(Line1.entity);

        // when
        final LineEntity result = lineDao.findById(id).get();

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("1호선"),
                () -> assertThat(result.getColor()).isEqualTo("파랑")
        );
    }

    @Test
    void 호선을_수정한다() {
        // given
        final Long id = lineDao.insert(Line1.entity);

        // when
        lineDao.updateById(new LineEntity(id, "1호선", "검정"));

        final LineEntity result = lineDao.findById(id).get();

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("1호선"),
                () -> assertThat(result.getColor()).isEqualTo("검정")
        );
    }

    @Test
    void 호선을_삭제한다() {
        // given
        final Long id = lineDao.insert(Line1.entity);
        lineDao.deleteById(id);

        // when, then
        assertThatThrownBy(() -> assertThat(lineDao.findById(id).get()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 등록된_호선을_이름으로_조회한다() {
        // given
        final Long id = lineDao.insert(Line1.entity);

        // when
        final LineEntity result = lineDao.findById(id).get();

        // then
        assertThat(result.getName()).isEqualTo("1호선");
    }
}
