package subway.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@Sql("/InitializeTable.sql")
@JdbcTest
class LineDaoTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("특정 이름과 색깔을 가진 노선이 있는지 확인한다.")
    void checkExistenceByNameAndColor() {
        assertAll(
            () -> assertThat(lineDao.checkExistenceByNameAndColor("1호선", "남색")).isTrue(),
            () -> assertThat(lineDao.checkExistenceByNameAndColor("4호선", "파랑")).isFalse()
        );
    }

    @Test
    @DisplayName("특정 id를 가진 노선이 있는지 확인한다.")
    void checkExistenceById() {
        assertAll(
            () -> assertThat(lineDao.checkExistenceById(1L)).isTrue(),
            () -> assertThat(lineDao.checkExistenceById(3L)).isFalse());
    }
}
