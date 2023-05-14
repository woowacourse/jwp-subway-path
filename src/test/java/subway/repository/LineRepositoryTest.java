package subway.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.exception.LineNotFoundException;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class LineRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        this.lineRepository = new LineRepository(new LineDao(jdbcTemplate));
    }
    
    @Test
    @DisplayName("노선 ID에 해당하는 노선 조회 시 노선 ID에 해당하는 노선이 존재하지 않으면 예외가 발생한다.")
    void findLineById_throw_not_found_lineId() {
        // given
        Long dummyLineId = -1L;

        // when, then
        assertThatThrownBy(() -> lineRepository.findLineById(dummyLineId))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선 ID에 해당하는 노선이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("노선 ID로 노선 삭제 시 노선 ID에 해당하는 노선이 존재하지 않으면 예외가 발생한다.")
    void removeLineById_throw_not_found_lineId() {
        // given
        Long dummyLineId = -1L;

        // when, then
        assertThatThrownBy(() -> lineRepository.removeLineById(dummyLineId))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선 ID에 해당하는 노선이 존재하지 않습니다.");
    }
}
