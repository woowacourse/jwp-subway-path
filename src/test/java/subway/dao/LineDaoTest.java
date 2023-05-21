package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.LineEntity;
import subway.exception.DuplicateLineException;

import java.util.List;
import java.util.Optional;

import static fixtures.LineFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void insertTest_success() {
        // given
        LineEntity entityToInsert = LINE7_INSERT_ENTITY;

        // when
        LineEntity insertedLineEntity = lineDao.insert(entityToInsert);

        // then
        assertThat(insertedLineEntity).isEqualTo(LINE7_FIND_ENTITY);
    }

    @Test
    @DisplayName("이미 존재하는 Line을 저장하려고 하면 예외가 발생한다.")
    void insertTest_fail() {
        // given
        LineEntity entityToInsert = LINE2_INSERT_ENTITY;

        // when, then
        assertThatThrownBy(() -> lineDao.insert(entityToInsert))
                .isInstanceOf(DuplicateLineException.class)
                .hasMessage("이미 존재하는 노선입니다.");
    }

    @Test
    @DisplayName("모든 노선 행을 조회한다.")
    void findAllTest() {
        // when
        List<LineEntity> findLineEntities = lineDao.findAll();

        // then
        assertThat(findLineEntities).isEqualTo(List.of(LINE2_FIND_ENTITY));
    }

    @Test
    @DisplayName("노선 id에 해당하는 행을 조회한다.")
    void findByIdTest() {
        // given
        Long lineId = LINE2_ID;

        // when
        LineEntity findLineEntity = lineDao.findById(lineId);

        // then
        assertThat(findLineEntity).isEqualTo(LINE2_FIND_ENTITY);
    }

    @Test
    @DisplayName("노선 이름에 해당하는 행을 조회한다.")
    void findByLineNameTest() {
        // given
        String lineName = LINE2_NAME;

        // when
        Optional<LineEntity> findLineEntity = lineDao.findByLineName(lineName);

        // then
        assertThat(findLineEntity).contains(LINE2_FIND_ENTITY);
    }

    @Test
    @DisplayName("노선 id에 해당하는 행을 삭제한다.")
    void deleteByIdTest() {
        // given
        Long lineId = LINE2_ID;

        // when
        lineDao.deleteById(lineId);

        // then
        assertThat(lineDao.findAll()).isEmpty();
    }
}