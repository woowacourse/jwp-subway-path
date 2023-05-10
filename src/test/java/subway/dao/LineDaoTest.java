package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

@JdbcTest
class LineDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @DisplayName("노선을 저장할 수 있다")
    @Test
    void insert() {
        //given
        final Line line = new Line("1호선", "blue");

        //when
        lineDao.insert(line);

        //then
        final int count = countRowsInTableWhere(jdbcTemplate, "line", "name='1호선' AND color='blue'");
        assertThat(count).isOne();
    }

    @DisplayName("중복된 이름을 넣었을 때 예외 발생")
    @Test
    void insertDuplicatedNameException() {
        //given
        final Line line = new Line("1호선", "blue");
        lineDao.insert(line);

        //when,then
        assertThatThrownBy(() -> lineDao.insert(line))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("모든 노선을 조회한다.")
    @Test
    void findAll() {
        //given
        final Line line = new Line("1호선", "blue");
        final Line line2 = new Line("2호선", "green");
        lineDao.insert(line);
        lineDao.insert(line2);

        //when
        final List<Line> lines = lineDao.findAll();

        //then
        assertThat(lines).hasSize(2);
    }

    @DisplayName("노선 id를 통해 특정 노선을 조회한다")
    @Test
    void findById() {
        //given
        final Line line = new Line("1호선", "blue");
        final Line insertedLine = lineDao.insert(line);

        //when, then
        assertThat(lineDao.findById(insertedLine.getId())).isPresent();
    }
}