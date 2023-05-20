package subway.dao;

import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import subway.SubwayJdbcFixture;
import subway.dao.dto.SectionStationResultMap;
import subway.dao.entity.SectionEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class SectionDaoTest extends SubwayJdbcFixture {

    @Test
    void 구간을_저장한다() {
        // given
        final SectionEntity sectionEntity = new SectionEntity(3, 삼성역, 선릉역, 3L);

        // when
        final Long id = sectionDao.insert(sectionEntity);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    void LineId로_구간을_조회한다() {
        // when
        final List<SectionStationResultMap> sections = sectionStationDao.findAllByLineId(이호선);

        // then
        assertThat(sections).hasSize(3);
    }

    @Test
    void 구간을_수정한다() {
        // given
        final Long targetId = sectionDao.insert(new SectionEntity(1, 삼성역, 석촌역, 3L));
        final SectionEntity newSection = new SectionEntity(targetId, 3, 선릉역, 잠실역, 3L);

        // when
        sectionDao.update(newSection);

        // then
        final SectionEntity sectionEntity = findById(targetId);
        assertAll(
                () -> assertThat(sectionEntity.getDistance()).isEqualTo(3),
                () -> assertThat(sectionEntity.getUpStationId()).isEqualTo(선릉역),
                () -> assertThat(sectionEntity.getDownStationId()).isEqualTo(잠실역)
        );
    }

    @Test
    void 구간을_삭제한다() {
        // given
        final Long id = sectionDao.insert(new SectionEntity(3, 잠실역, 10L, 팔호선));

        // when
        sectionDao.delete(id);

        // then
        assertThatThrownBy(() -> findById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
