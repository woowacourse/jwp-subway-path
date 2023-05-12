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
    private StationsDao stationsDao;

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
        assertThat(stationsDao.countStations(line))
                .as("처음에는 아무 역도 들어있지 않습니다.")
                .isEqualTo(0);

        // when
        stationsDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(5).build());

        // then
        assertThat(stationsDao.countStations(line))
                .as("두 개의 역이 동시에 등록되어 있습니다.")
                .isEqualTo(2);
    }

    @Test
    @DisplayName("노선에 역이 등록될 때 거리 정보도 함께 포함됩니다. 그래서 이웃한 역의 거리정보를 조회할 수 있습니다.")
    void distanceSaving() {
        // given
        stationsDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(5).build());

        // when & then
        assertThat(stationsDao.findDistanceBetween(stationS, stationJ, line))
                .isEqualTo(5);
    }

    @Test
    @DisplayName("거리를 조회할 두 역을 거꾸로 제공해도 거리를 계산할 수 있습니다.")
    void distanceSavingReverse() {
        // given
        stationsDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(5).build());

        // when & then
        assertThat(stationsDao.findDistanceBetween(stationJ, stationS, line))
                .isEqualTo(5);
    }

    @Test
    @DisplayName("거리 정보는 양의 정수로 제한합니다.")
    void distanceFormat() {
        // when && then
        assertThatThrownBy(() -> stationsDao.insert(Section.builder()
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

        stationsDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(6).build());
        stationsDao.insert(Section.builder()
                .line(line)
                .startingStation(stationO)
                .after(stationJ)
                .distance(3).build());

        // when & then
        Station stationY = stationDao.insert(new Station("양평"));
        assertThatCode(() -> stationsDao.insert(Section.builder()
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
        stationsDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(6).build());

        // when
        assertThatCode(() -> stationsDao.initialize(Section.builder()
                .line(line2)
                .startingStation(stationS)
                .after(stationO)
                .distance(6).build()))
                .as("송탄역을 1호선과 2호선 위에 동시에 올릴 수 있습니다.")
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("노선에서 역을 제거할 경우 정상 동작을 위해 재배치됩니다.")
    void delete() {
        // given
        sectionService.insert(line.getId(), stationS.getName(), stationJ.getName(), 6, true);
        sectionService.insert(line.getId(), stationO.getName(), stationJ.getName(), 3, true);
        assertThat(stationsDao.findByPreviousStation(stationS, line)
                .get().getNextStation())
                .as("원래 노선에 등록된 역은 3개고, 송탄역 다음은 오산역입니다.")
                .isEqualTo(stationO);

        // when
        stationsDao.deleteStation(stationO, line);

        // then
        assertThat(stationsDao.findByPreviousStation(stationS, line).get().getNextStation())
                .as("이제 남은 역은 2개이고, 송탄역 다음은 진위역입니다.")
                .isEqualTo(stationJ);
    }

    @Test
    @DisplayName("노선에서 역이 제거될 경우 역과 역 사이의 거리가 재배정됩니다.")
    void deleteDistance() {
        // given
        sectionService.insert(line.getId(), stationS.getName(), stationJ.getName(), 6, true);
        sectionService.insert(line.getId(), stationO.getName(), stationJ.getName(), 3, true);
        assertThatThrownBy(() -> stationsDao.findDistanceBetween(stationS, stationJ, line))
                .as("송탄역과 진위역은 이웃하지 않아 조회가 불가능합니다.")
                .isInstanceOf(IllegalArgumentException.class);

        // when
        stationsDao.deleteStation(stationO, line);

        // then
        assertThat(stationsDao.findDistanceBetween(stationS, stationJ, line))
                .as("이제 송탄역과 진위역이 이어져있고, 송탄-오산, 오산-진위 길이를 합한 6km의 길이를 가집니다.")
                .isEqualTo(6);
    }

    @Test
    @DisplayName("노선에 등록된 역이 2개인 경우 하나의 역을 제거할 때 두 역이 모두 제거됩니다.")
    void deleteAll() {
        // given
        sectionService.insert(line.getId(), stationS.getName(), stationJ.getName(), 6, true);
        assertThat(stationsDao.countStations(line))
                .as("2개의 역만 등록되어 있습니다.")
                .isEqualTo(2);

        // when
        stationsDao.deleteStation(stationO, line);

        // then
        assertThat(stationsDao.countStations(line))
                .as("둘만 남은 노선에서 하나를 지우면, 나머지 하나도 함께 지워져 0개가 됩니다.")
                .isEqualTo(0);
    }

    @Test
    @DisplayName("특정 노선에 등록된 역을 상행부터 순서대로 조회합니다.")
    void findAllOrderByUp() {
        stationsDao.initialize(Section.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(5).build());
        sectionService.insert(line.getId(), stationO.getName(), stationS.getName(), 6, true);

        assertThat(stationsDao.findAllOrderByUp(line))
                .containsExactly(stationO, stationS, stationJ);
    }
}