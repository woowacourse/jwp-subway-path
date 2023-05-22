package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;
import subway.domain.Subway;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SubwayRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM line");
        jdbcTemplate.execute("DELETE FROM station");
    }

    @Test
    void 모든_노선을_찾을_수_있다() {
        // given
        lineRepository.registerLine(new Line("8호선", "분홍색"));
        lineRepository.registerLine(new Line("2호선", "초록색"));

        // when
        final Subway subway = subwayRepository.findSubway();

        // then
        assertThat(subway.getLines()).containsExactly(
                new Line("8호선", "분홍색"),
                new Line("2호선", "초록색")
        );
    }
}
