package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @DisplayName("호선을 추가한다.")
    @Test
    void save() {
        // given
        LineEntity lineEntity = new LineEntity(1L, "1호선");

        // when
        LineEntity savedLineEntity = lineDao.save(lineEntity);

        // then
        assertThat(savedLineEntity.getName()).isEqualTo("1호선");
    }

    @DisplayName("전체 호선을 찾는다.")
    @Test
    void findAll() {
        // given
        LineEntity lineEntity1 = new LineEntity(1L, "1호선");
        LineEntity lineEntity2 = new LineEntity(2L, "2호선");
        lineDao.save(lineEntity1);
        lineDao.save(lineEntity2);

        // when
        List<LineEntity> lineEntities = lineDao.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(lineEntities.get(0).getName()).isEqualTo("1호선");
            softly.assertThat(lineEntities.get(1).getName()).isEqualTo("2호선");
        });
    }

    @DisplayName("id로 해당 호선을 찾는다.")
    @Test
    void findById() {
        // given
        LineEntity lineEntity = new LineEntity(1L, "1호선");
        LineEntity newLineEntity = lineDao.save(lineEntity);

        // when
        LineEntity savedLineEntity = lineDao.findById(newLineEntity.getId()).get();

        // then
        assertThat(savedLineEntity.getName()).isEqualTo("1호선");
    }

    @DisplayName("id에 해당되는 호선을 지운다.")
    @Test
    void deleteById() {
        // given
        Long lineId = 1L;
        LineEntity lineEntity = new LineEntity(lineId, "1호선");
        LineEntity newLineEntity = lineDao.save(lineEntity);

        // when
        lineDao.deleteById(newLineEntity.getId());

        // then
        assertThat(lineDao.findAll()).hasSize(0);
    }
}
