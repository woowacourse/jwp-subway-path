package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;

import java.util.Optional;

import static fixtures.LineFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class DBLineRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        LineDao lineDao = new LineDao(jdbcTemplate);
        lineRepository = new DBLineRepository(lineDao);
    }

    @Test
    @DisplayName("Line을 entity로 변환하여 저장한 후 저장된 entity를 Line으로 만들어서 반환한다.")
    void insertTest() {
        // given
        Line line = new Line(null, LINE7_NAME);
        Line expectLine = LINE7;

        // when
        Line insertedLine = lineRepository.insert(line);

        // then
        assertThat(insertedLine).isEqualTo(expectLine);
    }

    @Test
    @DisplayName("주어진 Line 이름과 일치하는 Line을 찾는다.")
    void findByLineNameTest_notNull() {
        // given
        String lineName = LINE2_NAME;
        Line expectLine = LINE2;

        // when
        Optional<Line> findLine = lineRepository.findByLineName(lineName);

        // then
        assertThat(findLine).contains(expectLine);
    }

    @Test
    @DisplayName("주어진 Line 이름과 일치하는 Line을 찾을 수 없다면 Optional.empty()를 반환한다.")
    void findByLineNameTest_null() {
        // given
        String lineName = LINE7_NAME;

        // when
        Optional<Line> findLine = lineRepository.findByLineName(lineName);

        // then
        assertThat(findLine).isEmpty();
    }

    @Test
    @DisplayName("주어진 Line을 삭제한다.")
    void removeTest() {
        // given
        Line line = LINE2;

        // when
        lineRepository.remove(line);

        // then
        assertThat(lineRepository.findByLineName(line.getName())).isEmpty();
    }
}