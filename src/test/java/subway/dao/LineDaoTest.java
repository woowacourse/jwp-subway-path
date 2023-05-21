package subway.dao;

import static fixtures.LineFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.line.Line;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    @DisplayName("노선을 저장한다.")
    void insertTest() {
        // given
        Line insertEntity = Line3.INSERT_LINE;

        // when
        Line insertedLine = lineDao.insert(insertEntity);

        // then
        assertThat(insertedLine).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(insertEntity);
    }

    @Test
    @DisplayName("노선 ID에 해당하는 행을 조회한다.")
    void findByIdTest() {
        // given
        long line2Id = INITIAL_Line2.ID;

        // when
        Optional<Line> findLine = lineDao.selectById(line2Id);

        // then
        assertThat(findLine.get()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(INITIAL_Line2.FIND_LINE);
    }

    @Test
    @DisplayName("노선 ID에 해당하는 행이 없으면 빈 Optional이 반환된다.")
    void findByIdEmptyOptional() {
        // given
        long dummyId = -1L;

        // when
        Optional<Line> findNullableLine = lineDao.selectById(dummyId);

        // then
        assertThat(findNullableLine.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("노선 이름에 해당하는 행을 조회한다.")
    void findByNameTest() {
        // given
        String line2Name = INITIAL_Line2.NAME;

        // when
        Optional<Line> findLine = lineDao.selectByName(line2Name);

        // then
        assertThat(findLine.get()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(INITIAL_Line2.FIND_LINE);
    }

    @Test
    @DisplayName("노선 이름에 해당하는 행이 없으면 빈 Optional이 반환된다.")
    void findByName_EmptyOptional() {
        // given
        String dummyName = "없는 노선";

        // when
        Optional<Line> findNullableLine = lineDao.selectByName(dummyName);

        // then
        assertThat(findNullableLine.isEmpty()).isTrue();
    }


    @DisplayName("모든 행을 조회한다.")
    @Test
    void findAll() {
        // when
        List<Line> lines = lineDao.selectAll();

        // then
        assertAll(
                () -> assertThat(lines.size()).isEqualTo(2),
                () -> assertThat(lines).usingRecursiveFieldByFieldElementComparator().contains(INITIAL_Line7.FIND_LINE, INITIAL_Line2.FIND_LINE)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"1:False", "-1:True"}, delimiter = ':')
    @DisplayName("노선 ID에 해당하는 행이 있으면 False, 없으면 True를 반환한다.")
    void isNotExistById(String id, Boolean expected) {
        // given
        Long parsedId = Long.parseLong(id);

        // when, then
        assertThat(lineDao.isNotExistById(parsedId)).isEqualTo(expected);
    }

    @DisplayName("노선 ID에 해당하는 행을 삭제한다.")
    @Test
    void deleteById() {
        // given
        long line2Id = INITIAL_Line2.ID;

        // when
        lineDao.deleteById(line2Id);
        Optional<Line> findNullableLine = lineDao.selectById(line2Id);

        // then
        assertThat(findNullableLine.isEmpty()).isTrue();
    }
}
