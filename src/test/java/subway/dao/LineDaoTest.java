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
import subway.domain.Line;
import subway.entity.LineEntity;

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
    @DisplayName("Line 삽입 테스트")
    void insert() {
        // given
        final String name = "2호선";
        final String color = "bg-green-500";
        // when
        final Long id = lineDao.insert(LineEntity.from(new Line(name, color)));
        // then
        assertThat(lineDao.findById(id)).isNotNull();
    }

    @Test
    @DisplayName("전체 Line 조회 테스트")
    void findAll() {
        // given
        final Long id1 = lineDao.insert(LineEntity.from(new Line("1호선", "bg-red-500")));
        final Long id2 = lineDao.insert(LineEntity.from(new Line("2호선", "bg-green-600")));
        // when
        final List<LineEntity> lineEntities = lineDao.findAll();
        // then
        assertAll(
                () -> assertThat(lineEntities).hasSize(2),
                () -> assertThat(lineEntities)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(
                                LineEntity.of(id1, "1호선", "bg-red-500"),
                                LineEntity.of(id2, "2호선", "bg-green-600")
                        ))
        );
    }

    @Test
    @DisplayName("id로 Line 조회 테스트")
    void findById() {
        // given
        final Long id = lineDao.insert(LineEntity.from(new Line("1호선", "bg-red-500")));
        // when
        final LineEntity lineEntity = lineDao.findById(id).orElseThrow();
        // then
        assertThat(lineEntity)
                .usingRecursiveComparison()
                .isEqualTo(LineEntity.of(id, "1호선", "bg-red-500"));
    }

    @Test
    @DisplayName("Line 수정 테스트")
    void update() {
        // given
        final Long id = lineDao.insert(LineEntity.from(new Line("1호선", "bg-red-500")));
        final String name = "2호선";
        final String color = "bg-green-500";
        // when
        lineDao.update(id, LineEntity.from(new Line(name, color)));
        // then
        assertThat(lineDao.findById(id).orElseThrow())
                .usingRecursiveComparison()
                .isEqualTo(LineEntity.of(id, name, color));
    }

    @Test
    @DisplayName("Line 삭제 테스트")
    void deleteById() {
        // given
        final Long id = lineDao.insert(LineEntity.from(new Line("1호선", "bg-red-500")));
        // when
        lineDao.deleteById(id);
        // then
        assertThat(lineDao.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("Line 존재 여부 테스트")
    void notExistsById() {
        // given
        final Long id = lineDao.insert(LineEntity.from(new Line("1호선", "bg-red-500")));
        // when
        final boolean isNotExists = lineDao.notExistsById(id);
        // then
        assertThat(isNotExists).isFalse();
    }
}
