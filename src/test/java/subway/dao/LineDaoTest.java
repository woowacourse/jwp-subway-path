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
        Line line = new Line("2", "green");
        Line insertedLine = lineDao.insert(line);
        assertThat(insertedLine.getName()).isEqualTo(line.getName());
        assertThat(insertedLine.getColor()).isEqualTo(line.getColor());
    }

    @DisplayName("이름이 같은 라인을 넣으면 예외를 던진다.")
    @Test
    void insertDuplicatedName() {
        Line line = new Line("2", "green");
        Line insertedLine = lineDao.insert(line);
        assertThatThrownBy(() -> lineDao.insert(line)).isInstanceOf(InvalidDataException.class);
        assertThat(insertedLine.getColor()).isEqualTo(line.getColor());
    }

    @DisplayName("전체 조회를 한다.")
    @Test
    void findAll() {
        Line firstLine = new Line("1", "blue");
        Line secondLine = new Line("2", "green");
        Line thirdLine = new Line("3", "orange");
        Line first = lineDao.insert(firstLine);
        Line second = lineDao.insert(secondLine);
        Line third = lineDao.insert(thirdLine);

        assertThat(lineDao.findAll().containsAll(List.of(first, second, third)));
    }

    @DisplayName("아이디로 조회한다.")
    @Test
    void findById() {
        Line firstLine = new Line("1", "blue");
        Line first = lineDao.insert(firstLine);

        assertThat(lineDao.findById(first.getId())).isEqualTo(first);
    }

    @DisplayName("없는 아이디로 조회하면 예외를 던진다.")
    @Test
    void findByNotExistId() {
        assertThatThrownBy(() -> lineDao.findById(10L)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("이름으로 조회한다.")
    @Test
    void findByName() {
        Line firstLine = new Line("1", "blue");
        Line first = lineDao.insert(firstLine);

        assertThat(lineDao.findByName(first.getName())).isEqualTo(first);
    }

    @DisplayName("없는 이름으로 조회하면 예외를 던진다.")
    @Test
    void findByNotExistName() {
        assertThatThrownBy(() -> lineDao.findByName("hardy")).isInstanceOf(NotFoundException.class);
    }


    @DisplayName("업데이트를 한다.")
    @Test
    void update() {
        Line line = new Line("1", "blue");
        Line originLine = lineDao.insert(line);
        lineDao.update(new Line(originLine.getId(), "2", "green"));
        assertThat(lineDao.findById(originLine.getId())).isNotEqualTo(originLine);
    }

    @DisplayName("삭제를 한다.")
    @Test
    void deleteById() {
        Line line = new Line("1", "blue");
        Line originLine = lineDao.insert(line);
        lineDao.deleteById(originLine.getId());
        assertThat(lineDao.findAll()).isEmpty();
    }
}
