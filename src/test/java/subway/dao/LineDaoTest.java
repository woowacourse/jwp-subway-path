package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("역이 없는 초기 노선을 생성한다.")
    void insert() {
        LineEntity lineEntity = new LineEntity(null, "1호선", 500);

        LineEntity savedLineEntity = lineDao.insert(lineEntity);

        assertAll(
                () -> assertThat(savedLineEntity.getId()).isNotNull(),
                () -> assertThat(savedLineEntity.getName()).isEqualTo(lineEntity.getName())
        );
    }

    @Test
    @DisplayName("ID에 해당하는 노선을 찾아온다.")
    void findById() {
        LineEntity lineEntity = new LineEntity(null, "1호선", 500);
        LineEntity savedLineEntity = lineDao.insert(lineEntity);

        LineEntity findLineEntity = lineDao.findById(savedLineEntity.getId()).get();
        assertThat(findLineEntity).isEqualTo(savedLineEntity);
    }

    @Test
    @DisplayName("이름에 해당하는 노선을 가져온다.")
    void findByName() {
        LineEntity lineEntity = new LineEntity(null, "1호선", 500);
        LineEntity savedLineEntity = lineDao.insert(lineEntity);

        LineEntity findLineEntity = lineDao.findByName(lineEntity.getName()).get();
        assertThat(findLineEntity).isEqualTo(savedLineEntity);
    }

    @Test
    @DisplayName("모든 노선을 가져온다.")
    void findAll() {
        LineEntity lineEntity1 = new LineEntity(null, "1호선", 500);
        LineEntity lineEntity2 = new LineEntity(null, "2호선", 300);
        LineEntity savedLineEntity1 = lineDao.insert(lineEntity1);
        LineEntity savedLineEntity2 = lineDao.insert(lineEntity2);

        List<LineEntity> lines = lineDao.findAll();

        assertThat(lines).containsExactly(savedLineEntity1, savedLineEntity2);
    }

    @Test
    @DisplayName("ID에 해당하는 노선을 삭제한다.")
    void deleteById() {
        LineEntity lineEntity = new LineEntity(null, "1호선", 300);
        LineEntity savedLineEntity = lineDao.insert(lineEntity);

        lineDao.deleteById(savedLineEntity.getId());

        assertThat(lineDao.findById(savedLineEntity.getId()).isEmpty()).isTrue();
    }
}
