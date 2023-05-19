package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LineDaoTest {

    private final RowMapper<LineEntity> lineEntityRowMapper = (rs, rn) -> new LineEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color")
    );

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        lineDao = new LineDao(dataSource);
    }

    @Test
    @DisplayName("Line 을 저장한다.")
    void save() {
        final String lineName = "디투당선";
        final String color = "bg_red_600";

        final LineEntity lineEntity = new LineEntity(lineName, color);

        lineDao.insert(lineEntity);

        final String sql = "SELECT * FROM line WHERE name = ?";
        final List<LineEntity> response = jdbcTemplate.query(sql, lineEntityRowMapper, lineName);
        assertThat(response).hasSize(1)
                .anyMatch(entity -> entity.getColor().equals(color));
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 ID 를 통해서 찾는다. (조회 성공)")
    @Sql("/line_test_data.sql")
    void findById_notEmpty() {
        List<LineEntity> lineEntity = lineDao.findById(1L);

        assertThat(lineEntity).hasSize(1)
                .anyMatch(entity -> entity.getId() == 1
                        && entity.getName().equals("2호선")
                        && entity.getColor().equals("bg-green-600"));
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 ID 를 통해서 찾는다. (조회 실패)")
    @Sql("/line_test_data.sql")
    void findById_empty() {
        List<LineEntity> lineEntity = lineDao.findById(3L);

        assertThat(lineEntity).isEmpty();
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 name 을 통해서 찾는다. (조회 성공)")
    @Sql("/line_test_data.sql")
    void findByName_notEmpty() {
        List<LineEntity> lineEntity = lineDao.findByName("2호선");

        assertThat(lineEntity).hasSize(1)
                .anyMatch(entity -> entity.getId() == 1
                        && entity.getName().equals("2호선")
                        && entity.getColor().equals("bg-green-600"));
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 name 을 통해서 찾는다. (조회 실패)")
    @Sql("/line_test_data.sql")
    void findByName_Empty() {
        List<LineEntity> lineEntity = lineDao.findByName("디투당선");

        assertThat(lineEntity).isEmpty();
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("모든 Line 을 조회한다.")
    @Sql("/line_test_data.sql")
    void findAll() {
        List<LineEntity> lines= lineDao.findAll();

        assertThat(lines).hasSize(2)
                .anyMatch(entity -> entity.getId() == 1L
                        && entity.getName().equals("2호선")
                        && entity.getColor().equals("bg-green-600"))
                .anyMatch(entity -> entity.getId() == 2L
                        && entity.getName().equals("8호선")
                        && entity.getColor().equals("bg-pink-600"));
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 update 한다. (성공)")
    @Sql("/line_test_data.sql")
    void update_success() {
        LineEntity lineEntity = new LineEntity(1L, "2호선", "bg-green-610");

        int updateCount = lineDao.update(lineEntity);

        List<LineEntity> lines = jdbcTemplate.query("SELECT * FROM line WHERE id = ?", lineEntityRowMapper, 1L);
        assertThat(updateCount).isEqualTo(1);
        assertThat(lines.get(0).getId()).isEqualTo(1L);
        assertThat(lines.get(0).getName()).isEqualTo("2호선");
        assertThat(lines.get(0).getColor()).isEqualTo("bg-green-610");
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 update 한다. (실패)")
    @Sql("/line_test_data.sql")
    void update_fail() {
        LineEntity lineEntity = new LineEntity(3L, "2호선", "bg-green-610");

        int updateCount = lineDao.update(lineEntity);

        assertThat(updateCount).isZero();
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 삭제한다. (성공)")
    @Sql("/line_test_data.sql")
    void delete_success() {
        int removeCount = lineDao.deleteById(1L);

        List<LineEntity> deleteLineEntity = jdbcTemplate.query("SELECT * FROM line WHERE id = ?", lineEntityRowMapper, 1L);
        assertThat(deleteLineEntity).isEmpty();
        assertThat(removeCount).isEqualTo(1);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 삭제한다. (실패)")
    @Sql("/line_test_data.sql")
    void delete_fail() {
        int removeCount = lineDao.deleteById(3L);

        assertThat(removeCount).isZero();
    }

}
