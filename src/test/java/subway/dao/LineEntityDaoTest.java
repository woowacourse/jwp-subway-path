package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.entity.LineEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
@Sql({"classpath:schema-test.sql"})
class LineEntityDaoTest {

    private LineDao lineDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void 호선을_생성한다() {
        // given
        LineEntity lineEntity = LineEntity.of("2호선", "초록");

        // when
        final Long id = lineDao.insert(lineEntity);

        // then
        assertThat(id).isPositive();
    }

    @Test
    void 모든_호선을_조회한다() {
        // given
        LineEntity lineEntity1 = LineEntity.of("1호선", "파랑");
        LineEntity lineEntity2 = LineEntity.of("2호선", "초록");
        LineEntity lineEntity3 = LineEntity.of("3호선", "노랑");
        lineDao.insert(lineEntity1);
        lineDao.insert(lineEntity2);
        lineDao.insert(lineEntity3);

        // when
        final List<LineEntity> results = lineDao.findAll();

        // then
        assertAll(
                () -> assertThat(results).extracting(LineEntity::getName)
                        .contains("1호선", "2호선", "3호선"),
                () -> assertThat(results).extracting(LineEntity::getColor)
                        .contains("파랑", "초록", "노랑")
        );
    }

    @Test
    void 호선을_ID로_조회한다() {
        // given
        LineEntity lineEntity = LineEntity.of("2호선", "초록");
        final Long id = lineDao.insert(lineEntity);

        // when
        final LineEntity result = lineDao.findById(id);

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("초록")
        );
    }

    @Test
    void 호선을_수정한다() {
        // given
        LineEntity lineEntity = LineEntity.of("2호선", "초록");
        final Long id = lineDao.insert(lineEntity);

        // when
        lineDao.updateById(id, LineEntity.of("2호선", "검정"));
        final LineEntity result = lineDao.findById(id);

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("검정")
        );
    }

    @Test
    void 호선을_삭제한다() {
        // given
        LineEntity lineEntity = LineEntity.of("2호선", "초록");
        final Long id = lineDao.insert(lineEntity);
        lineDao.deleteById(id);

        // when, then
        assertThrows(
                DataAccessException.class, () -> lineDao.findById(id)
        );
    }

}
