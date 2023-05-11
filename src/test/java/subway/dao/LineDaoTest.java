package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.LineEntity;

import java.util.Optional;

import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Line을 저장한다.")
    void insertTest() {
        // given
        LineEntity insertEntity = LINE7_INSERT_ENTITY;

        // when
        Long insertedLineId = lineDao.insert(insertEntity);

        // then
        assertThat(insertedLineId).isEqualTo(2L);
    }

    @Test
    @DisplayName("노선 이름에 해당하는 행을 조회한다.")
    void findByLineNameTest() {
        // given
        String lineName = DUMMY_LINE2_NAME;

        // when
        Optional<LineEntity> findLine = lineDao.findByLineName(lineName);

        // then
        assertThat(findLine.get()).isEqualTo(LINE2_FIND_ENTITY);
    }
}