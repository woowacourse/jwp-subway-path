package subway.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:schema.sql")
class SubwayMapRepositoryTest {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final SubwayMapRepository subwayMapRepository;

    private SubwayMapRepositoryTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.stationDao = new StationDao(jdbcTemplate);
        this.sectionDao = new SectionDao(jdbcTemplate);
        this.subwayMapRepository = new SubwayMapRepository(stationDao, sectionDao);
    }

    @Test
    @DisplayName("SubwayMap을 생성 및 조회 한다")
    void findByLineId() {
        // given
        final Station stationA = new Station("A");
        final Station stationB = new Station("B");
        final Station stationC = new Station("C");
        final Station insertStationA = stationDao.insert(stationA);
        final Station insertStationB = stationDao.insert(stationB);
        final Station insertStationC = stationDao.insert(stationC);
        final Section sectionAB = new Section(1, insertStationA.getId(), insertStationB.getId(), Direction.DOWN);
        final Section sectionBA = new Section(1, insertStationB.getId(), insertStationA.getId(), Direction.UP);
        final Section sectionBC = new Section(1, insertStationB.getId(), insertStationC.getId(), Direction.DOWN);
        final Section sectionCB = new Section(1, insertStationC.getId(), insertStationB.getId(), Direction.UP);
        final Line line = new Line(1L, "2호선", "green", null, null);
        sectionDao.insertSection(sectionAB, line.getId());
        sectionDao.insertSection(sectionBA, line.getId());
        sectionDao.insertSection(sectionBC, line.getId());
        sectionDao.insertSection(sectionCB, line.getId());

        // when
        final SubwayMap subwayMap = subwayMapRepository.findByLineId(line.getId());


        //then
        final Set<Station> stations = subwayMap.getSubwayMap().keySet();
        final List<Section> aSections = subwayMap.getSubwayMap().get(insertStationA);
        final List<Section> bSections = subwayMap.getSubwayMap().get(insertStationB);
        final List<Section> cSections = subwayMap.getSubwayMap().get(insertStationC);
        assertAll(
                () -> Assertions.assertThat(stations).extracting("name")
                        .containsExactly("A", "B", "C"),
                () -> Assertions.assertThat(aSections).extracting("departureId")
                        .containsExactly(insertStationA.getId()),
                () -> Assertions.assertThat(bSections).extracting("departureId")
                        .containsOnly(insertStationB.getId()),
                () -> Assertions.assertThat(cSections).extracting("departureId")
                        .containsExactly(insertStationC.getId())
        );
    }
}
