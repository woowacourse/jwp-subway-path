package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static subway.data.LineFixture.LINE2_ENTITY;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.data.LineFixture;
import subway.entity.LineEntity;

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

    @Test
    @DisplayName("노선 정보를 저장한다.")
    void line_data_insert() {
        // when
        LineEntity result = lineDao.insert(LINE2_ENTITY);

        // then
        assertThat(result.getName()).isEqualTo(LINE2_ENTITY.getName());
        assertThat(result.getColor()).isEqualTo(LINE2_ENTITY.getColor());
    }

}