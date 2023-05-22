package subway.persistance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.persistance.entity.SectionEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ContextConfiguration(classes = SectionDao.class)
@Sql("/schema.sql")
class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @Test
    @DisplayName("section을 저장한다")
    void insertSection() {
        // given
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(2L, "B");
        final Section section = new Section(stationA, stationB, new Distance(2));
        final Line line1 = new Line(1L, "1호선", "blue", new Sections());

        final SectionEntity sectionEntity = new SectionEntity(section, line1.getId(), 0);

        // when
        sectionDao.insertSection(sectionEntity);

        // then
        final SectionEntity found = sectionDao.findByLineId(line1.getId()).get(0);
        assertAll(
                () -> assertThat(found.getUpStationId()).isEqualTo(stationA.getId()),
                () -> assertThat(found.getDownStationId()).isEqualTo(stationB.getId()),
                () -> assertThat(found.getDistance()).isEqualTo(section.getDistance().getValue()),
                () -> assertThat(found.getLineId()).isEqualTo(line1.getId())
        );
    }

    @Test
    @DisplayName("section을 id로 조회한다")
    void findByLineId() {
        // given
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(2L, "B");
        final Station stationC = new Station(3L, "C");
        final Section sectionAB = new Section(stationA, stationB, new Distance(2));
        final Section sectionBC = new Section(stationB, stationC, new Distance(2));
        final Line line1 = new Line(1L, "1호선", "blue", new Sections());

        final SectionEntity sectionEntity = new SectionEntity(sectionAB, line1.getId(), 0);

        sectionDao.insertSection(new SectionEntity(sectionBC, 2L, 0));
        sectionDao.insertSection(sectionEntity);
        sectionDao.insertSection(new SectionEntity(sectionBC, 3L, 0));

        // when
        final SectionEntity found = sectionDao.findByLineId(line1.getId()).get(0);

        // then
        assertAll(
                () -> assertThat(found.getUpStationId()).isEqualTo(stationA.getId()),
                () -> assertThat(found.getDownStationId()).isEqualTo(stationB.getId()),
                () -> assertThat(found.getDistance()).isEqualTo(sectionAB.getDistance().getValue()),
                () -> assertThat(found.getLineId()).isEqualTo(line1.getId())
        );
    }

    @Test
    @DisplayName("id에 해당하는 section을 삭제한다")
    void deleteByLineId() {
        // given
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(2L, "B");
        final Station stationC = new Station(3L, "C");
        final Section sectionAB = new Section(stationA, stationB, new Distance(2));
        final Section sectionBC = new Section(stationB, stationC, new Distance(2));
        final Line line1 = new Line(1L, "1호선", "blue", new Sections());

        final SectionEntity sectionEntity = new SectionEntity(sectionAB, line1.getId(), 0);

        sectionDao.insertSection(new SectionEntity(sectionBC, 2L, 0));
        sectionDao.insertSection(sectionEntity);
        sectionDao.insertSection(new SectionEntity(sectionBC, 3L, 0));

        // when
        sectionDao.deleteByLineId(line1.getId());

        // then
        assertThat(sectionDao.findByLineId(line1.getId()).size()).isEqualTo(0);
    }
}
