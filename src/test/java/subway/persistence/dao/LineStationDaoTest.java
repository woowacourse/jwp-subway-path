package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.entity.LineStationEntity;


@JdbcTest
@AutoConfigureTestDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineStationDaoTest {

    private LineStationDao lineStationDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate,
               @Autowired DataSource dataSource) {
        lineStationDao = new LineStationDao(jdbcTemplate, dataSource);
    }

    @Test
    void 노선과_역의_관계를_저장한다() {
        final LineStationEntity lineStationEntity = LineStationEntity.of(1L, 1L);

        final LineStationEntity actual = lineStationDao.insert(lineStationEntity);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getLineId()).isEqualTo(1L);
            softAssertions.assertThat(actual.getStationId()).isEqualTo(1L);
        });
    }

    @Test
    void 모든_노선과_역의_관계를_조회한다() {
        final LineStationEntity lineStationEntity = LineStationEntity.of(1L, 1L);
        lineStationDao.insert(lineStationEntity);

        final List<LineStationEntity> actual = lineStationDao.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getLineId()).isEqualTo(1L);
            softAssertions.assertThat(actual.get(0).getStationId()).isEqualTo(1L);
        });
    }

    @Test
    void 노선과_역의_관계_하나를_조회한다() {
        final LineStationEntity lineStationEntity = LineStationEntity.of(1L, 1L);
        final LineStationEntity insertedLineStationEntity = lineStationDao.insert(lineStationEntity);

        final Optional<LineStationEntity> actual = lineStationDao.findById(insertedLineStationEntity.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().getId()).isPositive();
            softAssertions.assertThat(actual.get().getLineId()).isEqualTo(1L);
            softAssertions.assertThat(actual.get().getStationId()).isEqualTo(1L);
        });
    }

    @Test
    void 존재하지_않는_노선과_역의_관계를_조회하면_null을_반환한다() {
        final Optional<LineStationEntity> actual = lineStationDao.findById(-999L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 노선과_역의_관계_하나를_삭제한다() {
        final LineStationEntity lineStationEntity = LineStationEntity.of(1L, 1L);
        final LineStationEntity insertedLineStationEntity = lineStationDao.insert(lineStationEntity);

        final int actual = lineStationDao.deleteById(insertedLineStationEntity.getId());

        assertThat(actual).isOne();
    }
}
