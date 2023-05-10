package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.entity.SectionEntity;
import subway.domain.Direction;
import subway.domain.Line;
import subway.domain.Section;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ContextConfiguration(classes = SectionDao.class)
@Sql("classpath:schema.sql")
class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    private SectionDaoTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Section 입력 테스트")
    void insertSection() {
        // given
        final Section section = new Section(1, 1L, 2L, Direction.DOWN);
        final Line line = new Line(1L, "2호선", "green", null, null);

        // when
        sectionDao.insertSection(section, line.getId());

        // then
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(line.getId());
        assertThat(sectionEntities.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("LineId로 section을 조회한다.")
    void findByLineId() {
        // given
        final Section section = new Section(1, 1L, 2L, Direction.DOWN);
        final Line line = new Line(1L, "2호선", "green", null, null);
        sectionDao.insertSection(section, line.getId());

        // when
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(line.getId());

        // then
        assertThat(sectionEntities.get(0).getLineId()).isEqualTo(line.getId());
    }

    @Test
    @DisplayName("StationId로 Section을 조회한다")
    void findByStationIds() {
        // given
        final long departureId = 1L;
        final long arrivalId = 2L;
        final Section section = new Section(1, departureId, arrivalId, Direction.DOWN);
        final Line line = new Line(1L, "2호선", "green", null, null);
        sectionDao.insertSection(section, line.getId());

        // when
        final SectionEntity sectionEntity = sectionDao.findByStationIds(departureId, arrivalId);

        // then
        assertAll(
                () -> assertThat(sectionEntity.getDepartureId()).isEqualTo(departureId),
                () -> assertThat(sectionEntity.getArrivalId()).isEqualTo(arrivalId)
        );
    }

    @Test
    @DisplayName("Section의 정보를 수정한다")
    void update() {
        // given
        final Section section = new Section(1, 1L, 2L, Direction.DOWN);
        final Line line = new Line(1L, "2호선", "green", null, null);
        sectionDao.insertSection(section, line.getId());

        // when
        final Section updateSection = new Section(2, 1L, 3L, Direction.DOWN);
        sectionDao.update(line.getId(), updateSection);

        // then
        final SectionEntity entity = sectionDao.findByLineId(line.getId()).get(0);
        assertAll(
                () -> assertThat(entity.getDistance()).isEqualTo(updateSection.getDistance()),
                () -> assertThat(entity.getArrivalId()).isEqualTo(updateSection.getArrivalId())
        );
    }
}
