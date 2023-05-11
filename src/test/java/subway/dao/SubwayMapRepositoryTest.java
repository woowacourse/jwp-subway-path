package subway.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayMap;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.Fixture.*;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@JdbcTest
@Sql("classpath:schema.sql")
class SubwayMapRepositoryTest {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final SubwayMapRepository subwayMapRepository;

    private SubwayMapRepositoryTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.stationDao = new StationDao(jdbcTemplate);
        this.sectionDao = new SectionDao(jdbcTemplate);
        this.lineDao = new LineDao(jdbcTemplate);
        this.subwayMapRepository = new SubwayMapRepository(stationDao, sectionDao, lineDao);
    }

    @Test
    @DisplayName("SubwayMap을 생성 및 조회 한다")
    void find() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));
        subwayMapRepository.save(subwayMap1);

        // when
        final SubwayMap subwayMap = subwayMapRepository.find();

        //then
        final Set<Station> stations = subwayMap.getSubwayMap().keySet();
        final List<Section> aSections = subwayMap.getSubwayMap().get(stationA).getSections();
        final List<Section> bSections = subwayMap.getSubwayMap().get(stationB).getSections();
        final List<Section> cSections = subwayMap.getSubwayMap().get(stationC).getSections();
        assertAll(
                () -> Assertions.assertThat(stations).containsOnly(stationA, stationB, stationC),
                () -> Assertions.assertThat(aSections).containsOnly(sectionAB),
                () -> Assertions.assertThat(bSections).containsOnly(sectionBA, sectionBC),
                () -> Assertions.assertThat(cSections).containsOnly(sectionCB)
        );
    }
}
