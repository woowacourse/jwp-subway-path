package subway.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    @DisplayName("findByLineName() : 라인 이름으로 라인을 찾을 수 있다.")
    void test_findByLineName() throws Exception {
        //given
        final String lineName = "1호선";

        //when
        final Optional<LineEntity> savedLine = lineDao.findByLineName(lineName);

        //then
        assertAll(
                () -> assertThat(savedLine).isPresent(),
                () -> assertEquals(1L, savedLine.get().getId()),
                () -> assertEquals(savedLine.get().getName(), lineName)
        );
    }

    @Test
    @DisplayName("findAll() : 모든 라인을 찾을 수 있다.")
    void test_findAll() throws Exception {
        //when
        final List<LineEntity> lineEntities = lineDao.findAll();

        //then
        assertEquals(2, lineEntities.size());
    }

    @Test
    @DisplayName("findByLineId() : 라인 id로 라인을 찾을 수 있다.")
    void test_findByLineId() throws Exception {
        //given
        final Long lineId = 2L;

        //when
        final Optional<LineEntity> savedLine = lineDao.findByLineId(lineId);

        //then
        assertAll(
                () -> Assertions.assertThat(savedLine).isPresent(),
                () -> assertEquals(2L, savedLine.get().getId()),
                () -> assertEquals("2호선", savedLine.get().getName())
        );
    }

    @Test
    @DisplayName("deleteById() : 라인 id로 라인을 삭제할 수 있다.")
    void test_deleteById() throws Exception {
        //given
        final Long lineId = 2L;

        final int beforeSize = lineDao.findAll().size();

        //when
        lineDao.deleteById(lineId);

        //then
        final int afterSize = lineDao.findAll().size();
        assertEquals(afterSize, beforeSize - 1);
    }

    @Test
    @DisplayName("save() : 라인을 저장할 수 있다.")
    void test_save() throws Exception {
        //given
        final LineEntity lineEntity = new LineEntity("newLine");

        final int beforeSize = lineDao.findAll().size();

        //when
        lineDao.save(lineEntity);

        //then
        final int afterSize = lineDao.findAll().size();

        assertEquals(afterSize, beforeSize + 1);
    }
}
