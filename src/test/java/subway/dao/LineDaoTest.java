package subway.dao;

import static fixtures.LineFixtures.Line2;
import static fixtures.LineFixtures.Line7;
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
        Line insertEntity = Line7.INSERT_ENTITY;

        // when
        Line insertedLine = lineDao.insert(insertEntity);

        // then
        assertThat(insertedLine).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(Line7.FIND_ENTITY);
    }

    @Test
    @DisplayName("노선 ID에 해당하는 행을 조회한다.")
    void findByIdTest() {
        // given
        long line2Id = Line2.ID;

        // when
        Optional<Line> findLine = lineDao.findById(line2Id);

        // then
        assertThat(findLine.get()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(Line2.FIND_ENTITY);
    }

    @Test
    @DisplayName("노선 ID에 해당하는 행이 없으면 빈 Optional이 반환된다.")
    void findByIdEmptyOptional() {
        // given
        long line7Id = Line7.ID;

        // when
        Optional<Line> findNullableLine = lineDao.findById(line7Id);

        // then
        assertThat(findNullableLine.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("노선 이름에 해당하는 행을 조회한다.")
    void findByLineNameTest() {
        // given
        String lineName = Line2.NAME;

        // when
        Optional<Line> findLine = lineDao.findByLineName(lineName);

        // then
        assertThat(findLine.get()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(Line2.FIND_ENTITY);
    }

    @Test
    @DisplayName("노선 이름에 해당하는 행이 없으면 빈 Optional이 반환된다.")
    void findByLineNameEmptyOptional() {
        // given
        String line7Name = Line7.NAME;

        // when
        Optional<Line> findNullableLine = lineDao.findByLineName(line7Name);

        // then
        assertThat(findNullableLine.isEmpty()).isTrue();
    }

    @DisplayName("모든 행을 조회한다.")
    @Test
    void findAll() {
        // when
        List<Line> lines = lineDao.findAll();
        Line line2 = lines.get(0);

        // then
        assertAll(
                () -> assertThat(lines.size()).isEqualTo(1),
                () -> assertThat(line2).usingRecursiveComparison().ignoringFields("id").isEqualTo(Line2.FIND_ENTITY)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"1:False", "2:True"}, delimiter = ':')
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
        long line2Id = Line2.ID;

        // when
        lineDao.deleteById(line2Id);
        Optional<Line> findNullableLine = lineDao.findById(line2Id);

        // then
        assertThat(findNullableLine.isEmpty()).isTrue();
    }
}
