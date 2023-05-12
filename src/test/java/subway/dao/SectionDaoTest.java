package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.entity.SectionEntity;
import subway.domain.Section;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.Fixture.*;

@JdbcTest
@Sql("classpath:schema.sql")
class SectionDaoTest {

    private final SectionDao sectionDao;

    private SectionDaoTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Section 입력 테스트")
    void insertSection() {
        // given
        // when
        sectionDao.insertSection(sectionAB);

        // then
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(line.getId());
        assertThat(sectionEntities.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("LineId로 section을 조회한다.")
    void findByLineId() {
        // given
        sectionDao.insertSection(sectionAB);

        // when
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(line.getId());

        // then
        assertThat(sectionEntities.get(0).getLineId()).isEqualTo(line.getId());
    }

    @Test
    @DisplayName("StationId로 Section을 조회한다")
    void findByStationIds() {
        // given
        sectionDao.insertSection(sectionAB);

        // when
        final SectionEntity sectionEntity = sectionDao.findByStationIds(stationA.getId(), stationB.getId());

        // then
        assertAll(
                () -> assertThat(sectionEntity.getDepartureId()).isEqualTo(stationA.getId()),
                () -> assertThat(sectionEntity.getArrivalId()).isEqualTo(stationB.getId())
        );
    }

    @Test
    @DisplayName("Section의 정보를 수정한다")
    void update() {
        // given
        sectionDao.insertSection(sectionAB);

        // when
        final Section updateSection = new Section(2, stationA, stationC, line);
        sectionDao.update(sectionAB, updateSection);

        // then
        final SectionEntity entity = sectionDao.findByLineId(line.getId()).get(0);
        assertAll(
                () -> assertThat(entity.getDistance()).isEqualTo(updateSection.getDistance()),
                () -> assertThat(entity.getArrivalId()).isEqualTo(updateSection.getArrival().getId())
        );
    }
}
