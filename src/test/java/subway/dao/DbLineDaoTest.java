package subway.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;
import subway.entity.LineEntity;
import subway.fixture.LineFixture;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class DbLineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private DbLineDao dbLineDao;

    @BeforeEach
    void setUp() {
        dbLineDao = new DbLineDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("노선 저장하기")
    void save() {
        dbLineDao.saveLine(new LineEntity("1호선"));

        assertThat(dbLineDao.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 노선 정보 조회하기")
    void findAll() {
        dbLineDao.saveLine(new LineEntity("1호선"));
        dbLineDao.saveLine(new LineEntity("2호선"));

        assertThat(dbLineDao.findAll().size()).isEqualTo(2);
    }
}