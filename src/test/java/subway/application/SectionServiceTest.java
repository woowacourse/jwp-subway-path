package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({SectionService.class, SectionDao.class, LineDao.class, StationDao.class})
class SectionServiceTest {
    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    private Line lineOne;
    Station stationS;
    Station stationJ;
    Station stationO;

    @BeforeEach
    void setUp() {
        lineOne = lineDao.insert(new Line("1호선", "blue"));
        stationS = stationDao.insert(new Station("송탄"));
        stationJ = stationDao.insert(new Station("진위"));
        stationO = stationDao.insert(new Station("오산"));
        sectionService.insert(lineOne.getId(), "송탄", "진위", Distance.of(6), true);
    }

    @Test
    @DisplayName("노선에 역이 하나도 등록되지 않은 상황에서 최초 등록시 두 역이 동시 등록됩니다.")
    void initialize() {
        assertThat(sectionDao.countStations(lineOne))
                .as("두 개의 역이 동시에 등록되어 있습니다.")
                .isEqualTo(2);
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리 정보를 고려해야 합니다. - 상행방향")
    void distanceChange() {
        // A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
        // B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.

        // given
        assertThat(sectionService.findDistanceBetween(stationS, stationJ, lineOne))
                .as("처음 송탄과 진위 사이의 거리는 6km.")
                .isEqualTo(Distance.of(6));

        // when
        sectionService.insert(lineOne.getId(), stationO.getName(), stationJ.getName(), Distance.of(2), true);

        // then
        assertThat(sectionService.findDistanceBetween(stationS, stationO, lineOne))
                .as("송탄과 오산 사이의 거리는 4km.")
                .isEqualTo(Distance.of(4));
        assertThat(sectionService.findDistanceBetween(stationO, stationJ, lineOne))
                .as("오산과 진위 사이의 거리는 2km.")
                .isEqualTo(Distance.of(2));
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리 정보를 고려해야 합니다. - 하행방향")
    void distanceChange2() {
        // A-B-C 노선에서 A 다음에 D 역을 등록하려고 하는데
        // B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.

        // given
        assertThat(sectionService.findDistanceBetween(stationS, stationJ, lineOne))
                .as("처음 송탄과 진위 사이의 거리는 6km.")
                .isEqualTo(Distance.of(6));

        // when
        sectionService.insert(lineOne.getId(), stationO.getName(), stationS.getName(), Distance.of(2), false);

        // then
        assertThat(sectionService.findDistanceBetween(stationS, stationO, lineOne))
                .as("송탄과 오산 사이의 거리는 4km.")
                .isEqualTo(Distance.of(2));
        assertThat(sectionService.findDistanceBetween(stationO, stationJ, lineOne))
                .as("오산과 진위 사이의 거리는 2km.")
                .isEqualTo(Distance.of(4));
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리는 양의 정수라는 비즈니스 규칙을 지켜야 합니다.")
    void distanceRule() {
        // A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
        // B-C역의 거리가 3km인 경우 B-D 거리는 3km보다 적어야 합니다.
        // B-C가 3km인데 B-D거리가 3km면 D-C거리는 0km가 되어야 하는데 거리는 양의 정수여야 하기 때문에 이 경우 등록이 불가능 해야합니다.
        // when
        assertThatThrownBy(() -> sectionService.insert(lineOne.getId(), stationO.getName(), stationJ.getName(), Distance.of(6), true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리 정보는 양의 정수로 제한합니다.");
    }

    @Test
    @DisplayName("노선에 역이 등록될 때 거리 정보도 함께 포함됩니다. 그래서 이웃한 역의 거리정보를 조회할 수 있습니다.")
    void distanceSaving() {
        // when & then
        assertThat(sectionService.findDistanceBetween(stationS, stationJ, lineOne)).isEqualTo(Distance.of(6));
    }

    @Test
    @DisplayName("거리를 조회할 두 역을 거꾸로 제공해도 거리를 계산할 수 있습니다.")
    void distanceSavingReverse() {
        // when & then
        assertThat(sectionService.findDistanceBetween(stationJ, stationS, lineOne)).isEqualTo(Distance.of(6));
    }

    @Test
    @DisplayName("노선에서 역을 제거할 경우 정상 동작을 위해 재배치됩니다.")
    void delete() {
        // given
        sectionService.insert(lineOne.getId(), stationO.getName(), stationJ.getName(), Distance.of(3), true);
        assertThat(sectionDao.findByPreviousStation(stationS, lineOne)
                .get().getNextStation())
                .as("원래 노선에 등록된 역은 3개고, 송탄역 다음은 오산역입니다.")
                .isEqualTo(stationO);

        // when
        sectionService.deleteStation(lineOne.getId(), stationO.getName());

        // then
        assertThat(sectionDao.findByPreviousStation(stationS, lineOne).get().getNextStation())
                .as("이제 남은 역은 2개이고, 송탄역 다음은 진위역입니다.")
                .isEqualTo(stationJ);
    }

    @Test
    @DisplayName("노선에서 역이 제거될 경우 역과 역 사이의 거리가 재배정됩니다.")
    void deleteDistance() {
        // given
        sectionService.insert(lineOne.getId(), stationO.getName(), stationJ.getName(), Distance.of(3), true);
        assertThatThrownBy(() -> sectionService.findDistanceBetween(stationS, stationJ, lineOne))
                .as("송탄역과 진위역은 이웃하지 않아 조회가 불가능합니다.")
                .isInstanceOf(IllegalArgumentException.class);

        // when
        sectionService.deleteStation(lineOne.getId(), stationO.getName());

        // then
        assertThat(sectionService.findDistanceBetween(stationS, stationJ, lineOne))
                .as("이제 송탄역과 진위역이 이어져있고, 송탄-오산, 오산-진위 길이를 합한 6km의 길이를 가집니다.")
                .isEqualTo(Distance.of(6));
    }

    @Test
    @DisplayName("노선에 등록된 역이 2개인 경우 하나의 역을 제거할 때 두 역이 모두 제거됩니다.")
    void deleteAll() {
        // given
        assertThat(sectionDao.countStations(lineOne))
                .as("2개의 역만 등록되어 있습니다.")
                .isEqualTo(2);

        // when
        sectionService.deleteStation(lineOne.getId(), stationO.getName());

        // then
        assertThat(sectionDao.countStations(lineOne))
                .as("둘만 남은 노선에서 하나를 지우면, 나머지 하나도 함께 지워져 0개가 됩니다.")
                .isEqualTo(0);
    }

    @Test
    @DisplayName("하나의 역은 여러 노선에 등록될 수 있습니다.")
    void multipleSubwayMap() {
        // given
        Line lineTwo = lineDao.insert(new Line("2호선", "yellow"));

        // when
        assertThatCode(() -> sectionService.insert(lineTwo.getId(), stationS.getName(), stationO.getName(), Distance.of(6), false))
                .as("송탄역을 1호선과 2호선 위에 동시에 올릴 수 있습니다.")
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("역을 처음 초기화하면 노선 정보에 상행 종점 역 정보가 저장되어 있다.")
    void head() {
        final var headStation = lineDao.findHeadStation(lineOne);
        assertThat(headStation).isEqualTo(stationS);
    }

    @Test
    @DisplayName("역의 상행 종점 역 정보가 바뀌면, 노선 정보에 상행 종점 역 정보도 변경된다.")
    void changeHead() {
        assertThat(lineDao.findHeadStation(lineOne))
                .as("본래는 송탄이 상행 최종역이지만")
                .isEqualTo(stationS);
        sectionService.insert(lineOne.getId(), "오산", "송탄", Distance.of(4), true);
        assertThat(lineDao.findHeadStation(lineOne))
                .as("오산을 송탄 앞에 배치한 이후로는 오산이 상행 최종역이다.")
                .isEqualTo(stationO);
    }
}