package subway.line.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@JdbcTest
class LineDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private LineDao lineDao;
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  @BeforeEach
  void setUp() {
    lineDao = new LineDao(jdbcTemplate);
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("LINE")
        .usingGeneratedKeyColumns("id");
  }

  @Test
  void insert() {
    final Long insertId = lineDao.insert(new LineEntity("2호선"));

    final Optional<LineEntity> lineEntity = lineDao.findById(insertId);

    assertNotNull(lineEntity);
  }

  @Test
  void findByName() {
    final Long insertId = lineDao.insert(new LineEntity("2호선"));

    final Optional<LineEntity> lineEntity = lineDao.findByName("2호선");

    assertNotNull(lineEntity);
  }
}
