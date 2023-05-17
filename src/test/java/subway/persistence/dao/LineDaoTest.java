package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.entity.LineEntity;

@JdbcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class LineDaoTest {

    LineDao lineDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired DataSource dataSource) {
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    void insert_메소드는_line을_저장하고_저장한_데이터를_반환한다() {
        final LineEntity lineEntity = LineEntity.of("12호선", "bg-red-500");

        final LineEntity actual = lineDao.insert(lineEntity);

        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getName()).isEqualTo(lineEntity.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(lineEntity.getColor())
        );
    }

    @Test
    void findAll_메소드는_모든_line을_반환한다() {
        final LineEntity lineEntity = LineEntity.of("12호선", "bg-red-500");
        lineDao.insert(lineEntity);

        final List<LineEntity> actual = lineDao.findAll();

        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void findById_메소드는_저장되어_있는_id를_전달하면_해당_line을_반환한다() {
        final LineEntity lineEntity = LineEntity.of("12호선", "bg-red-500");
        final LineEntity persistLineEntity = lineDao.insert(lineEntity);

        final Optional<LineEntity> actual = lineDao.findById(persistLineEntity.getId());

        assertThat(actual).isPresent();
    }

    @Test
    void findById_메소드는_없는_id를_전달하면_빈_Optional을_반환한다() {
        final Optional<LineEntity> actual = lineDao.findById(-999L);

        assertThat(actual).isEmpty();
    }

    @Test
    void existsByName_메소드는_저장되어_있는_이름을_전달하면_true를_반환한다() {
        final LineEntity lineEntity = LineEntity.of("12호선", "bg-red-500");
        final LineEntity persistLineEntity = lineDao.insert(lineEntity);

        final boolean actual = lineDao.existsByName(persistLineEntity.getName());

        assertThat(actual).isTrue();
    }

    @Test
    void existsByName_메소드는_없는_이름을_전달하면_false를_반환한다() {
        final boolean actual = lineDao.existsByName("abc");

        assertThat(actual).isFalse();
    }

    @Test
    void deleteById_메소드는_id를_전달하면_해당_id를_가진_line을_삭제한다() {
        final LineEntity lineEntity = LineEntity.of("12호선", "bg-red-500");
        final LineEntity persistLineEntity = lineDao.insert(lineEntity);

        assertDoesNotThrow(() -> lineDao.deleteById(persistLineEntity.getId()));
    }
}
