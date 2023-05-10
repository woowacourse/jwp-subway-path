package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dao.StationsDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Stations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import({StationsService.class, StationsDao.class, LineDao.class, StationDao.class})
class StationsServiceTest {
    @Autowired
    private StationsService stationsService;

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
        stationsDao.initialize(Stations.builder()
                .line(line)
                .startingStation(stationS)
                .before(stationJ)
                .distance(6).build());
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리 정보를 고려해야 합니다. - 상행방향")
    void distanceChange() {
        // A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
        // B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.

        // given
        assertThat(stationsDao.findDistanceBetween(stationS, stationJ, line))
                .as("처음 송탄과 진위 사이의 거리는 6km.")
                .isEqualTo(6);

        // when
        stationsService.insert(line.getId(), stationO.getName(), stationJ.getName(), 2, true);

        // then
        assertThat(stationsDao.findDistanceBetween(stationS, stationO, line))
                .as("송탄과 오산 사이의 거리는 4km.")
                .isEqualTo(4);
        assertThat(stationsDao.findDistanceBetween(stationO, stationJ, line))
                .as("오산과 진위 사이의 거리는 2km.")
                .isEqualTo(2);
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리 정보를 고려해야 합니다. - 하행방향")
    void distanceChange2() {
        // A-B-C 노선에서 A 다음에 D 역을 등록하려고 하는데
        // B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.

        // given
        assertThat(stationsDao.findDistanceBetween(stationS, stationJ, line))
                .as("처음 송탄과 진위 사이의 거리는 6km.")
                .isEqualTo(6);

        // when
        stationsService.insert(line.getId(), stationO.getName(), stationS.getName(), 2, false);

        // then
        assertThat(stationsDao.findDistanceBetween(stationS, stationO, line))
                .as("송탄과 오산 사이의 거리는 4km.")
                .isEqualTo(2);
        assertThat(stationsDao.findDistanceBetween(stationO, stationJ, line))
                .as("오산과 진위 사이의 거리는 2km.")
                .isEqualTo(4);
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리는 양의 정수라는 비즈니스 규칙을 지켜야 합니다.")
    void distanceRule() {
        // A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
        // B-C역의 거리가 3km인 경우 B-D 거리는 3km보다 적어야 합니다.
        // B-C가 3km인데 B-D거리가 3km면 D-C거리는 0km가 되어야 하는데 거리는 양의 정수여야 하기 때문에 이 경우 등록이 불가능 해야합니다.
        // when
        assertThatThrownBy(() -> stationsService.insert(line.getId(), stationO.getName(), stationJ.getName(), 6, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 역과 역 사이는 언제나 양의 정수를 유지해야 합니다.");
    }
}