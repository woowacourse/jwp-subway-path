package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.exception.InvalidLineException;

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

    @Nested
    @DisplayName("노선 ID로 조회 시 ")
    class FindById {

        @Test
        @DisplayName("존재하는 노선이라면 노선 정보를 반환한다.")
        void findById() {
            //given
            final Line line = lineRepository.save(new Line("2호선", "초록색"));

            //when
            final Line result = lineRepository.findById(line.getId());

            //then
            assertAll(
                    () -> assertThat(result.getId()).isEqualTo(line.getId()),
                    () -> assertThat(result.getName()).isEqualTo(line.getName()),
                    () -> assertThat(result.getColor()).isEqualTo(line.getColor())
            );
        }

        @Test
        @DisplayName("존재하지 않는 노선이라면 예외를 던진다.")
        void findByInvalidId() {
            //given
            //when
            //then
            assertThatThrownBy(() -> lineRepository.findById(-2L))
                    .isInstanceOf(InvalidLineException.class)
                    .hasMessage("존재하지 않는 노선 ID 입니다.");
        }
    }
}
