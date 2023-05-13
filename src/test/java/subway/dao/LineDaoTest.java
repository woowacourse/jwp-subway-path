package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class LineDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @DisplayName("라인을 추가한다.")
    @Test
    void insert() {
        // given
        Line line = new Line("2", "green");

        // when
        Line insertedLine = lineDao.insert(line);

        // then
        assertThat(insertedLine.getName()).isEqualTo(line.getName());
        assertThat(insertedLine.getColor()).isEqualTo(line.getColor());
    }

    @DisplayName("이름이 같은 라인을 넣으면 예외를 던진다.")
    @Test
    void insertDuplicatedName() {
        // given
        Line line = new Line("2", "green");

        // when
        Line insertedLine = lineDao.insert(line);

        // then
        assertThatThrownBy(() -> lineDao.insert(line)).isInstanceOf(InvalidDataException.class);
        assertThat(insertedLine.getColor()).isEqualTo(line.getColor());
    }

    @DisplayName("전체 조회를 한다.")
    @Test
    void findAll() {
        // given
        Line firstLine = new Line("1", "blue");
        Line secondLine = new Line("2", "green");
        Line thirdLine = new Line("3", "orange");

        // when
        Line first = lineDao.insert(firstLine);
        Line second = lineDao.insert(secondLine);
        Line third = lineDao.insert(thirdLine);

        // then
        assertThat(lineDao.findAll().containsAll(List.of(first, second, third)));
    }

    @DisplayName("아이디로 조회한다.")
    @Test
    void findById() {
        // given
        Line firstLine = new Line("1", "blue");

        // when
        Line first = lineDao.insert(firstLine);

        // then
        assertThat(lineDao.findById(first.getId())).isEqualTo(first);
    }

    @DisplayName("없는 아이디로 조회하면 예외를 던진다.")
    @Test
    void findByNotExistId() {
        // when, then
        assertThatThrownBy(() -> lineDao.findById(10L)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("이름으로 조회한다.")
    @Test
    void findByName() {
        // given
        Line firstLine = new Line("1", "blue");

        // when
        Line first = lineDao.insert(firstLine);

        // then
        assertThat(lineDao.findByName(first.getName())).isEqualTo(first);
    }

    @DisplayName("없는 이름으로 조회하면 예외를 던진다.")
    @Test
    void findByNotExistName() {
        // when, then
        assertThatThrownBy(() -> lineDao.findByName("hardy")).isInstanceOf(NotFoundException.class);
    }


    @DisplayName("업데이트를 한다.")
    @Test
    void update() {
        // given
        Line line = new Line("1", "blue");
        Line originLine = lineDao.insert(line);

        // when
        lineDao.update(new Line(originLine.getId(), "2", "green"));

        // then
        assertThat(lineDao.findById(originLine.getId()).getName()).isNotEqualTo(originLine.getName());
        assertThat(lineDao.findById(originLine.getId()).getColor()).isNotEqualTo(originLine.getColor());
    }

    @DisplayName("삭제를 한다.")
    @Test
    void deleteById() {
        // given
        Line line = new Line("1", "blue");
        Line originLine = lineDao.insert(line);

        // when
        lineDao.deleteById(originLine.getId());

        // then
        assertThat(lineDao.findAll()).isEmpty();
    }
}
