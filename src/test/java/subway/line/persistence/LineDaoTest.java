package subway.line.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;;

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
  void insert() {
    final Long insertId = lineDao.insert(new LineEntity("2호선"));

    final Optional<LineEntity> lineEntity = lineDao.findById(insertId);

    assertNotNull(lineEntity);
  }

  @Test
  void findById() {
    final LineEntity expected = new LineEntity("2호선");
    final Long insertId = lineDao.insert(expected);

    final Optional<LineEntity> lineEntity = lineDao.findById(insertId);

    Assertions.assertThat(expected)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(lineEntity.get());
  }

  @Test
  void findAll() {
    lineDao.insert(new LineEntity("2호선"));
    lineDao.insert(new LineEntity("3호선"));

    final List<LineEntity> lines = lineDao.findAll();

    Assertions.assertThat(lines.size()).isEqualTo(2);
  }

  @Test
  void findByName() {
    lineDao.insert(new LineEntity("2호선"));

    final Optional<LineEntity> lineEntity = lineDao.findByName("2호선");

    assertNotNull(lineEntity);
  }
}
