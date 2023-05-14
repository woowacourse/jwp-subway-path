package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.dao.entity.LineEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineDaoTest extends DaoTestConfig {

    LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void 노선을_저장한다() {
        // when
        final Long saveLineId = lineDao.insert("2", "초록");

        // expect
        assertThat(saveLineId)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 노선을_조회한다() {
        // given
        final Long saveLineId = lineDao.insert("2", "초록");

        // when
        final Optional<LineEntity> maybeLine = lineDao.findByLineId(saveLineId);

        // then
        assertAll(
                () -> assertThat(maybeLine).isPresent(),
                () -> assertThat(maybeLine.get())
                        .usingRecursiveComparison()
                        .isEqualTo(new LineEntity(saveLineId, "2", "초록"))
        );
    }

    @Test
    void 전체_노선을_조회한다() {
        // given
        final Long saveBlueLineId = lineDao.insert("1", "파랑");
        final Long saveGreenLineId = lineDao.insert("2", "초록");

        // when
        final List<LineEntity> findLines = lineDao.findAll();

        // then
        assertThat(findLines)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(
                        new LineEntity(saveBlueLineId, "1", "파랑"),
                        new LineEntity(saveBlueLineId, "2", "초록")
                );
    }

    @Test
    void 노선_이름으로_노선을_조회한다() {
        // given
        final Long saveBlueLineId = lineDao.insert("1", "파랑");
        final Long saveGreenLineId = lineDao.insert("2", "초록");

        // when
        final Optional<LineEntity> findLineEntity = lineDao.findByLineName("1");

        // then
        assertThat(findLineEntity)
                .contains(new LineEntity(saveBlueLineId, "1", "파랑"));
    }

    @Test
    void 노선을_삭제한다() {
        // given
        final Long saveLineId = lineDao.insert("2", "초록");

        // when
        lineDao.deleteByLineId(saveLineId);

        final Optional<LineEntity> maybeLine = lineDao.findByLineId(saveLineId);

        // then
        assertThat(maybeLine).isEmpty();
    }
}
