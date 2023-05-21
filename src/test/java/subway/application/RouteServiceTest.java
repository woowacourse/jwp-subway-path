package subway.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;
import static subway.fixture.LineFixture.FIXTURE_LINE_1;
import static subway.fixture.LineFixture.FIXTURE_LINE_2;
import static subway.fixture.LineFixture.FIXTURE_LINE_3;
import static subway.fixture.SectionFixture.LINE1_SECTIONS;
import static subway.fixture.SectionFixture.LINE2_SECTIONS;
import static subway.fixture.SectionFixture.LINE3_SECTIONS;
import static subway.fixture.StationFixture.FIXTURE_STATION_1;
import static subway.fixture.StationFixture.FIXTURE_STATION_9;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.exception.RequestDataNotFoundException;

@DisplayName("지하철 경로 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private LineDao lineDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        routeService = new RouteService(lineDao, sectionDao, stationDao);
    }

    @DisplayName("최단 경로 조회 시 해당 경로의 총 거리와 요금을 계산해 반환한다")
    @Test
    void findShortestRoute() {
        // given
        when(stationDao.findById(1L)).thenReturn(Optional.of(FIXTURE_STATION_1));
        when(stationDao.findById(9L)).thenReturn(Optional.of(FIXTURE_STATION_9));
        when(lineDao.findAll()).thenReturn(List.of(FIXTURE_LINE_1, FIXTURE_LINE_2, FIXTURE_LINE_3));
        when(sectionDao.findByLineId(1L)).thenReturn(LINE1_SECTIONS);
        when(sectionDao.findByLineId(2L)).thenReturn(LINE2_SECTIONS);
        when(sectionDao.findByLineId(3L)).thenReturn(LINE3_SECTIONS);

        // when, then
        assertDoesNotThrow(() -> routeService.findShortestRoute(1L, 9L));
    }

    @DisplayName("최단 경로 조회 시 출발 역이 DB에 존재하지 않으면 예외를 발생한다")
    @Test
    void findShortestRouteFailNotExistingSource() {
        // given
        when(stationDao.findById(1L)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> routeService.findShortestRoute(1L, 2L))
                .isInstanceOf(RequestDataNotFoundException.class)
                .hasMessageContaining("출발 역이 존재하지 않습니다.");
    }

    @DisplayName("최단 경로 조회 시 도착 역이 DB에 존재하지 않으면 예외를 발생한다")
    @Test
    void findShortestRouteFailNotExistingTarget() {
        // given
        when(stationDao.findById(1L)).thenReturn(Optional.of(FIXTURE_STATION_1));
        when(stationDao.findById(2L)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> routeService.findShortestRoute(1L, 2L))
                .isInstanceOf(RequestDataNotFoundException.class)
                .hasMessageContaining("도착 역이 존재하지 않습니다.");
    }
}
