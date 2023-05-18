package subway.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.data.LineFixture.LINE2_ENTITY;
import static subway.data.LineFixture.LINE3_ENTITY;

@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DELETE FROM line");
    }


    @Test
    @DisplayName("노선 정보를 저장한다.")
    void line_data_insert() {
        // when
        LineEntity result = lineDao.insert(LINE2_ENTITY);

        // then
        assertThat(result.getName()).isEqualTo(LINE2_ENTITY.getName());
        assertThat(result.getColor()).isEqualTo(LINE2_ENTITY.getColor());
    }

    @Test
    @DisplayName("노선 정보를 노선이름으로 불러온다.")
    void line_data_load_by_name() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);

        // when
        Optional<LineEntity> result = lineDao.findByName(insertedLine.getName());

        // then
        assertThat(result.get().getId()).isEqualTo(insertedLine.getId());
        assertThat(result.get().getName()).isEqualTo(insertedLine.getName());
        assertThat(result.get().getColor()).isEqualTo(insertedLine.getColor());
    }

    @Test
    @DisplayName("노선 전체 정보를 불러온다.")
    void line_all_data_load() {
        // given
        LineEntity insertedLine2 = lineDao.insert(LINE2_ENTITY);
        LineEntity insertedLine3 = lineDao.insert(LINE3_ENTITY);

        List<LineEntity> expect = List.of(insertedLine2, insertedLine3);

        // when
        List<LineEntity> result = lineDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expect);
    }
}
