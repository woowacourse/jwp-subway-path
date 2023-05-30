package subway.persistence.dao;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.persistence.entity.LineEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixtures.entity.LineEntityFixture.GYEONGUI_CENTRAL_LINE_ENTITY;

@SuppressWarnings("NonAsciiCharacters")
class LineDaoTest extends DaoTest {
    @Test
    void 노선을_저장한다() {
        // given, when
        final LineEntity actual = lineDao.insert(GYEONGUI_CENTRAL_LINE_ENTITY);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("경의중앙선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-blue-500");
        });
    }

    @Test
    void 모든_노선을_조회한다() {
        // given, when
        lineDao.insert(GYEONGUI_CENTRAL_LINE_ENTITY);
        final List<LineEntity> actual = lineDao.findAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo("경의중앙선");
            softAssertions.assertThat(actual.get(0).getColor()).isEqualTo("bg-blue-500");
        });
    }

    @Test
    void 노선_하나를_조회한다() {
        // given
        final LineEntity insertedLineEntity = lineDao.insert(GYEONGUI_CENTRAL_LINE_ENTITY);

        // when
        final LineEntity actual = lineDao.findById(insertedLineEntity.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("경의중앙선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-blue-500");
        });
    }

    @Test
    void 노선_하나를_삭제한다() {
        // given
        final LineEntity insertedLineEntity = lineDao.insert(GYEONGUI_CENTRAL_LINE_ENTITY);

        // when
        final int actual = lineDao.deleteById(insertedLineEntity.getId());

        // then
        assertThat(actual).isOne();
    }
}
