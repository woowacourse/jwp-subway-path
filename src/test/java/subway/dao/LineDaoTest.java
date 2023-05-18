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
import subway.exception.LineNotFoundException;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
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

//    @Test
//    @DisplayName("이름으로 id 조회 성공")
//    @Sql("/line_test_data.sql")
//    void findIdByName_success() {
//        // given
//        final String name = "2호선";
//
//        // when
//        final LineEntity entity = lineDao.findByName(name);
//
//        // then
//        assertThat(entity.getId()).isEqualTo(1L);
//    }
//
//    @Test
//    @DisplayName("이름으로 id 조회 실패 - 존재하지 않는 이름 입력")
//    @Sql("/line_test_data.sql")
//    void findIdByName_fail_name_not_found() {
//        // given
//        final String name = "포비";
//
//        // expected
//        assertThatThrownBy(() -> lineDao.findByName(name))
//                .isInstanceOf(LineNotFoundException.class);
//    }

}
