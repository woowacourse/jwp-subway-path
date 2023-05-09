package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DisplayName("Line Dao")
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
    @DisplayName("저장 성공")
    void save_success() {
        // given
        final String lineName = "디투당선";
        final String color = "bg_red_600";

        final LineEntity lineEntity = new LineEntity(lineName, color);

        // when
        lineDao.insert(lineEntity);

        // then
        final String sql = "SELECT * FROM line WHERE name = ?";
        final List<LineEntity> response = jdbcTemplate.query(sql, lineEntityRowMapper, lineName);

        assertThat(response).hasSize(1)
                .anyMatch(entity -> entity.getColor().equals(color));
    }

}
