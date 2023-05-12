package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.application.SectionService;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Section;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Sql("/schema.sql")
//@JdbcTest()
//@Import({StationsDao.class, LineDao.class, StationDao.class, StationsService.class})
class SectionDaoTest {
    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    private Line line;
    Station stationS;
    Station stationJ;
    Station stationO;

    @BeforeEach
    void setUp() {
        line = lineDao.insert(new Line("1호선", "blue"));
        stationS = stationDao.insert(new Station("송탄"));
        stationJ = stationDao.insert(new Station("진위"));
        stationO = stationDao.insert(new Station("오산"));
    }

    @Test
    @DisplayName("노선에 역이 하나도 등록되지 않은 상황에서 최초 등록시 두 역이 동시 등록됩니다.")
    void initialize() {
        // given
        assertThat(sectionDao.countStations(line))
                .as("처음에는 아무 역도 들어있지 않습니다.")
                .isEqualTo(0);

        // when
        sectionDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(5).build());

        // then
        assertThat(sectionDao.countStations(line))
                .as("두 개의 역이 동시에 등록되어 있습니다.")
                .isEqualTo(2);
    }

    @Test
    @DisplayName("거리 정보는 양의 정수로 제한합니다.")
    void distanceFormat() {
        // when && then
        assertThatThrownBy(() -> sectionDao.insert(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(-3).build())
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 역과 역 사이는 언제나 양의 정수를 유지해야 합니다.");
    }

    @ParameterizedTest(name = "{displayName} - {0}")
    @DisplayName("A-B-C역이 등록되어 있는 노선의 어디에든 D역을 등록할 수 있습니다.")
    @ValueSource(ints = {0, 1, 2})
    void savingD(int index) {
        // given
        List<Station> stations = List.of(stationS, stationJ, stationO);

        sectionDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(6).build());
        sectionDao.insert(Section.builder()
                .line(line)
                .startingStation(stationO)
                .after(stationJ)
                .distance(3).build());

        // when & then
        Station stationY = stationDao.insert(new Station("양평"));
        assertThatCode(() -> sectionDao.insert(Section.builder()
                .line(line)
                .startingStation(stationY)
                .after(stations.get(index))
                .distance(2).build())
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("하나의 역은 여러 노선에 등록될 수 있습니다.")
    void multipleSubwayMap() {
        // given
        Line line2 = lineDao.insert(new Line("2호선", "yellow"));
        sectionDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(6).build());

        // when
        assertThatCode(() -> sectionDao.initialize(Section.builder()
                .line(line2)
                .startingStation(stationS)
                .after(stationO)
                .distance(6).build()))
                .as("송탄역을 1호선과 2호선 위에 동시에 올릴 수 있습니다.")
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("특정 노선에 등록된 역을 상행부터 순서대로 조회합니다.")
    void findAllOrderByUp() {
        sectionDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(5).build());
        sectionService.insert(line.getId(), stationO.getName(), stationS.getName(), 6, true);

        assertThat(sectionDao.findAllOrderByUp(line))
                .containsExactly(stationO, stationS, stationJ);
    }
}