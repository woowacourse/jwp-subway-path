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

    @DisplayName("Line을 저장한다.")
    @Test
    void save() {
        // when
        Long newId = lineDao.save(new LineEntity("3호선"));

        // then
        assertThat(newId).isEqualTo(id + 1);
    }

    @DisplayName("Line 이름을 입력받아 해당하는 Line Entity 를 반환한다.")
    @Test
    void findByName() {
        // when
        LineEntity newLineEntity = lineDao.findByName("2호선");

        // then
        assertThat(newLineEntity.getId()).isEqualTo(id);
    }

    @DisplayName("Line id를 입력받아 해당하는 Line Entity 를 반환한다.")
    @Test
    void findById() {
        // when
        LineEntity newLineEntity = lineDao.findById(id);

        // then
        assertThat(newLineEntity.getId()).isEqualTo(id);
    }

    @DisplayName("모든 Line을 반환한다.")
    @Test
    void findAll() {
        // given
        lineDao.save(new LineEntity("3호선"));
        lineDao.save(new LineEntity("4호선"));
        lineDao.save(new LineEntity("5호선"));

        // when
        List<LineEntity> lineEntities = lineDao.findAll();

        // then
        assertThat(lineEntities).hasSize(4);
    }

    @DisplayName("Line id를 입력받아 일치하는 Line을 삭제한다.")
    @Test
    void deleteById() {
        // when
        int deleteRowNumber = lineDao.deleteById(id);

        // then
        assertThat(deleteRowNumber).isEqualTo(1);
    }

    @DisplayName("중복되는 이름인지 확인한다.")
    @Test
    void isExisted() {
        // when
        boolean duplicate = lineDao.isExisted("2호선");

        // then
        assertThat(duplicate).isTrue();
    }
}
