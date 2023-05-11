package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.line.Line;

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
    @DisplayName("식별자로 역을 조회할 수 있다.")
    void findById_test() {
        // given
        final Line 이호선 = new Line("이호선", "bg-red-600");
        final Line 저장된_이호선 = lineDao.insert(이호선);

        final Long 저장된_이호선_아이디 = 저장된_이호선.getId();

        // when
        final Optional<Line> line = lineDao.findById(저장된_이호선_아이디);

        // then
        assertAll(
                () -> assertThat(line).isNotNull(),
                () -> assertThat(line.get().getName()).isEqualTo("이호선"),
                () -> assertThat(line.get().getColor()).isEqualTo("bg-red-600")
        );
    }

}
