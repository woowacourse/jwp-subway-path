package subway.persistence.dao;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.persistence.entity.SectionEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixtures.entity.SectionEntityFixture.ONE_ONE_TWO_FIVE_SECTION_ENTITY;
import static subway.fixtures.entity.SectionEntityFixture.ONE_TWO_THREE_FIVE_SECTION_ENTITY;

@SuppressWarnings("NonAsciiCharacters")
class SectionDaoTest extends DaoTest {

    @Test
    void 역과_역의_관계를_저장한다() {
        final SectionEntity actual = sectionDao.insert(ONE_ONE_TWO_FIVE_SECTION_ENTITY);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getLineId()).isEqualTo(1L);
            softAssertions.assertThat(actual.getUpStationId()).isEqualTo(1L);
            softAssertions.assertThat(actual.getDownStationId()).isEqualTo(2L);
            softAssertions.assertThat(actual.getDistance()).isEqualTo(5);
        });
    }

    @Test
    void 모든_역과_역의_관계를_저장한다() {
        assertDoesNotThrow(() -> sectionDao.insertAll(List.of(ONE_ONE_TWO_FIVE_SECTION_ENTITY, ONE_TWO_THREE_FIVE_SECTION_ENTITY)));
    }

    @Test
    void 모든_라인의_모든_역과_역의_관계를_조회한다() {
        // given
        sectionDao.insert(ONE_ONE_TWO_FIVE_SECTION_ENTITY);

        // when
        final List<SectionEntity> actual = sectionDao.findAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getLineId()).isEqualTo(1L);
            softAssertions.assertThat(actual.get(0).getUpStationId()).isEqualTo(1L);
            softAssertions.assertThat(actual.get(0).getDownStationId()).isEqualTo(2L);
            softAssertions.assertThat(actual.get(0).getDistance()).isEqualTo(5);
        });
    }

    @Test
    void 특정_라인에_해당하는_모든_역과_역의_관계를_조회한다() {
        // given
        sectionDao.insert(ONE_ONE_TWO_FIVE_SECTION_ENTITY);

        // when
        final List<SectionEntity> actual = sectionDao.findAllByLineId(1L);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getLineId()).isEqualTo(1L);
            softAssertions.assertThat(actual.get(0).getUpStationId()).isEqualTo(1L);
            softAssertions.assertThat(actual.get(0).getDownStationId()).isEqualTo(2L);
            softAssertions.assertThat(actual.get(0).getDistance()).isEqualTo(5);
        });
    }

    @Test
    void 역과_역의_관계_하나를_조회한다() {
        // given
        final SectionEntity insertedSectionEntity = sectionDao.insert(ONE_ONE_TWO_FIVE_SECTION_ENTITY);

        // when
        final SectionEntity actual = sectionDao.findById(insertedSectionEntity.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getLineId()).isEqualTo(1L);
            softAssertions.assertThat(actual.getUpStationId()).isEqualTo(1L);
            softAssertions.assertThat(actual.getDownStationId()).isEqualTo(2L);
            softAssertions.assertThat(actual.getDistance()).isEqualTo(5);
        });
    }

    @Test
    void 특정_지하철_역을_가지는_구간을_모두_조회한다() {
        // given
        sectionDao.insertAll(List.of(ONE_ONE_TWO_FIVE_SECTION_ENTITY, ONE_TWO_THREE_FIVE_SECTION_ENTITY));

        // when
        final List<SectionEntity> sectionEntities = sectionDao.findAllByStationId(1L);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(sectionEntities).hasSize(1);
            softAssertions.assertThat(sectionEntities.get(0).getLineId()).isEqualTo(1L);
            softAssertions.assertThat(sectionEntities.get(0).getUpStationId()).isEqualTo(1L);
            softAssertions.assertThat(sectionEntities.get(0).getDownStationId()).isEqualTo(2L);
        });
    }

    @Test
    void 특정_노선에_해당하는_관계를_모두_삭제한다() {
        // given
        sectionDao.insertAll(List.of(ONE_ONE_TWO_FIVE_SECTION_ENTITY, ONE_TWO_THREE_FIVE_SECTION_ENTITY));

        // when
        final int actual = sectionDao.deleteByLineId(1L);

        // then
        assertThat(actual).isEqualTo(2);
    }
}
