package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate);
    }

    @DisplayName("노선을 저장할 수 있다")
    @Test
    void save() {
        //given
        final Line line = new Line("1호선", "red");

        //when
        lineDao.insert(line);

        //then
        assertThat(countRowsInTable(jdbcTemplate, "line")).isOne();
    }

    @DisplayName("중복된 이름으로 노선 추가 시 예외가 발생한다")
    @Test
    void save_fail() {
        //given
        final Line line = new Line("1호선", "red");
        lineDao.insert(line);

        //when, then
        assertThatThrownBy(() -> lineDao.insert(line))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름은 중복될 수 없습니다.");
    }

    @DisplayName("노선 id로 해당 노선을 찾을 수 있다")
    @Test
    void findById() {
        //given
        final Line line = new Line("1호선", "red");
        final Long id = lineDao.insert(line).getId();

        //when
        final Line persisted = lineDao.findById(id);

        //then
        assertAll(
                () -> assertThat(persisted.getName()).isEqualTo(line.getName()),
                () -> assertThat(persisted.getColor()).isEqualTo(line.getColor()));
    }

    @DisplayName("해당 노선 id의 노선이 존재하지 않을 경우 예외가 발생한다")
    @Test
    void findById_fail() {
        //given
        final Line line = new Line("1호선", "red");
        lineDao.insert(line).getId();

        //when, then
        assertThatThrownBy(() -> lineDao.findById(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 노선이 존재하지 않습니다.");
    }
}
