package subway.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.domain.Line;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LineRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM line");
        jdbcTemplate.execute("DELETE FROM station");
    }

    @Test
    void 이름으로_노선을_찾을_수_있다() {
        // given
        final String name = "8호선";
        lineRepository.registerLine(new Line(name, "분홍색"));

        // when
        final Line line = lineRepository.findLineByName(name);

        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("8호선"),
                () -> assertThat(line.getColor()).isEqualTo("분홍색")
        );
    }

    @Test
    void 존재하지_않는_노선의_이름을_찾으면_예외가_발생한다() {
        // given
        assertThatThrownBy(() -> lineRepository.findLineByName("상상역"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 이름을 가진 노선이 존재하지 않습니다.");
    }

    @Test
    void 노선을_등록할_수_있다() {
        // when
        lineRepository.registerLine(new Line("8호선", "분홍색"));

        // then
        final Line line = lineRepository.findLineByName("8호선");
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("8호선"),
                () -> assertThat(line.getColor()).isEqualTo("분홍색")
        );
    }

    @Test
    void id로_노선을_조회할_수_있다() {
        // given
        final Long id = lineDao.insert("8호선", "분홍색");

        // when
        final Line line = lineRepository.findLineById(id);

        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("8호선"),
                () -> assertThat(line.getColor()).isEqualTo("분홍색")
        );
    }

    @Test
    void 존재하지_않는_노선의_id를_찾으면_예외가_발생한다() {
        // given
        assertThatThrownBy(() -> lineRepository.findLineById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("노선 정보가 잘못되었습니다.");
    }
}