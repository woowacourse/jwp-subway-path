package subway.persistence.dao;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.entity.LineEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineDaoTest {

    private LineDao lineDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate,
               @Autowired DataSource dataSource) {
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    void 노선을_저장한다() {
        final LineEntity lineEntity = LineEntity.of("1호선", "bg-red-500");

        final LineEntity actual = lineDao.insert(lineEntity);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("1호선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-red-500");
        });
    }

    @Test
    void 모든_노선을_조회한다() {
        final LineEntity lineEntity = LineEntity.of("1호선", "bg-red-500");
        lineDao.insert(lineEntity);

        final List<LineEntity> actual = lineDao.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo("1호선");
            softAssertions.assertThat(actual.get(0).getColor()).isEqualTo("bg-red-500");
        });
    }

    @Test
    void 노선_하나를_조회한다() {
        final LineEntity lineEntity = LineEntity.of("1호선", "bg-red-500");
        final LineEntity insertedLineEntity = lineDao.insert(lineEntity);

        final LineEntity actual = lineDao.findById(insertedLineEntity.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("1호선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-red-500");
        });
    }

    @Test
    void 노선_하나를_삭제한다() {
        final LineEntity lineEntity = LineEntity.of("1호선", "bg-red-500");
        final LineEntity insertedLineEntity = lineDao.insert(lineEntity);

        final int actual = lineDao.deleteById(insertedLineEntity.getId());

        assertThat(actual).isOne();
    }
}
