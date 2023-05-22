package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class LineDaoTest {

    private LineDao lineDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    @DisplayName("line을 추가한다.")
    void insert() {
        Long lineId = lineDao.insert(new Line(1L, "1호선"));

        assertThat(lineId).isEqualTo(lineDao.findById(lineId).get().getId());
    }

    @Test
    @DisplayName("line전체를 조회한다.")
    void findAll() {
        Long lineId = lineDao.insert(new Line(1L, "1호선"));
        Long lineId2 = lineDao.insert(new Line(2L, "2호선"));
        List<Line> lines = lineDao.findAll();

        assertAll(
                () -> assertThat(lines.size()).isEqualTo(2),
                () -> assertThat(lines.get(0).getName()).isEqualTo("1호선"),
                () -> assertThat(lines.get(0).getId()).isEqualTo(lineId),
                () -> assertThat(lines.get(1).getName()).isEqualTo("2호선"),
                () -> assertThat(lines.get(1).getId()).isEqualTo(lineId2)
        );
    }

    @Test
    @DisplayName("line의 Id로 조회한다.")
    void findById() {
        Long lineId = lineDao.insert(new Line(1L, "2호선"));

        Line line = lineDao.findById(lineId).get();

        assertAll(
                () -> assertThat(line.getName()).isEqualTo("2호선"),
                () -> assertThat(line.getId()).isEqualTo(lineId)
        );
    }
}
