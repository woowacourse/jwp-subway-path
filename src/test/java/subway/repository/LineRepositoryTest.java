package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;

@JdbcTest
class LineRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository = new LineRepository(new LineDao(jdbcTemplate), new SectionDao(jdbcTemplate));
    }

    @Test
    @DisplayName("노선을 저장한다.")
    void save() {
        //given
        final Line line = new Line("2호선", "초록색");

        //when
        final Line result = lineRepository.save(line);

        //then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("초록색")
        );
    }
}
