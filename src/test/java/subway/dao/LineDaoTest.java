package subway.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class LineDaoTest {

    LineDao lineDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    @DisplayName("새로운 노선을 등록한다.")
    void save() {
        //given
        LineEntity lineEntity = new LineEntity(1L, "1호선");

        //when
        lineDao.save(lineEntity);
        LineEntity searchedLine = lineDao.findLineById(1).get();

        //then
        assertThat(searchedLine).usingRecursiveComparison().ignoringFields("id").isEqualTo(lineEntity);
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void findAll() {
        //given
        LineEntity line1 = new LineEntity(1L, "1호선");
        LineEntity line2 = new LineEntity(2L, "2호선");

        lineDao.save(line1);
        lineDao.save(line2);

        //when
        List<LineEntity> lines = lineDao.findAll();

        //then
        assertThat(lines).hasSize(2);
    }

    @Test
    @DisplayName("노선의 이름으로 특정 노선을 조회한다.")
    void findLineByName() {
        //given
        LineEntity line1 = new LineEntity(1L, "1호선");
        LineEntity line2 = new LineEntity(2L, "2호선");

        lineDao.save(line1);
        lineDao.save(line2);

        //when
        LineEntity searchedLine = lineDao.findLineByName(line1.getName()).get();

        //then
        assertThat(searchedLine).usingRecursiveComparison().isEqualTo(line1);
    }

    @Test
    @DisplayName("노선의 ID로 특정 노선을 조회한다.")
    void findLineByID() {
        //given
        LineEntity line1 = new LineEntity(1L, "1호선");
        LineEntity line2 = new LineEntity(2L, "2호선");

        lineDao.save(line1);
        lineDao.save(line2);

        //when
        LineEntity searchedLine = lineDao.findLineById(line1.getId()).get();

        //then
        assertThat(searchedLine).usingRecursiveComparison().isEqualTo(line1);
    }

    @Test
    @DisplayName("특정 노선을 삭제한다.")
    void deleteLineById() {
        //given
        LineEntity line1 = new LineEntity(1L, "1호선");
        LineEntity line2 = new LineEntity(2L, "2호선");

        lineDao.save(line1);
        lineDao.save(line2);

        //when
        lineDao.deleteLineById(1L);
        List<LineEntity> lines = lineDao.findAll();

        //then
        assertThat(lines).hasSize(1);
    }

    @AfterEach
    void resetTable() {
        jdbcTemplate.update("TRUNCATE TABLE line");
        jdbcTemplate.update("alter table line alter column id restart");
    }

}