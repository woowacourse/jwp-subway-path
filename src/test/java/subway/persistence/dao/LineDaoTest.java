package subway.persistence.dao;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.persistence.entity.LineEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class LineDaoTest extends DaoTest {
    @Test
    void 노선을_저장한다() {
        final LineEntity lineEntity = LineEntity.of("경의중앙선", "bg-blue-500");

        final LineEntity actual = lineDao.insert(lineEntity);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("경의중앙선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-blue-500");
        });
    }

    @Test
    void 모든_노선을_조회한다() {
        final LineEntity lineEntity = LineEntity.of("경의중앙선", "bg-red-500");
        lineDao.insert(lineEntity);

        final List<LineEntity> actual = lineDao.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo("경의중앙선");
            softAssertions.assertThat(actual.get(0).getColor()).isEqualTo("bg-red-500");
        });
    }

    @Test
    void 노선_하나를_조회한다() {
        final LineEntity lineEntity = LineEntity.of("경의중앙선", "bg-red-500");
        final LineEntity insertedLineEntity = lineDao.insert(lineEntity);

        final LineEntity actual = lineDao.findById(insertedLineEntity.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("경의중앙선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-red-500");
        });
    }

    @Test
    void 노선_하나를_삭제한다() {
        final LineEntity lineEntity = LineEntity.of("경의중앙선", "bg-red-500");
        final LineEntity insertedLineEntity = lineDao.insert(lineEntity);

        final int actual = lineDao.deleteById(insertedLineEntity.getId());

        assertThat(actual).isOne();
    }
}
