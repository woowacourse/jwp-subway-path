package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.LineEntity;

@JdbcTest
class LineDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private Long id;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        LineEntity lineEntity = new LineEntity("2호선");
        id = lineDao.save(lineEntity);
    }

    @Test
    @DisplayName("저장한다.")
    void save() {
        // when
        Long newId = lineDao.save(new LineEntity("3호선"));

        // expected
        assertThat(newId).isEqualTo(id + 1);
    }

    @Test
    @DisplayName("Line 이름을 입력받아 해당하는 Line Entity 를 반환한다.")
    void findByName() {
        // when
        LineEntity newLineEntity = lineDao.findByName("2호선");

        // expected
        assertThat(newLineEntity.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Line id를 입력받아 해당하는 Line Entity 를 반환한다.")
    void findById() {
        // when
        LineEntity newLineEntity = lineDao.findById(id);

        // expected
        assertThat(newLineEntity.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("모든 Line을 반환한다.")
    void findAll() {
        // given
        lineDao.save(new LineEntity("3호선"));
        lineDao.save(new LineEntity("4호선"));
        lineDao.save(new LineEntity("5호선"));

        // when
        List<LineEntity> lineEntities = lineDao.findAll();

        // expected
        assertThat(lineEntities).hasSize(4);
    }

    @Test
    @DisplayName("Line id를 입력받아 일치하는 Line을 삭제한다.")
    void deleteByName() {
        // when
        int deleteRowNumber = lineDao.deleteById(id);

        // expected
        assertThat(deleteRowNumber).isEqualTo(1);
    }
}
