package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.StationDao;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.fare.DistanceFareStrategy;
import subway.domain.fare.FareCalculator;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.dto.StationResponse;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationDao stationDao;

    @Mock
    private LineRepository lineRepository;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(stationDao, lineRepository, new FareCalculator(new DistanceFareStrategy()));
    }

    @DisplayName("요청된 역의 source와 destination에 맞는 최단 경로와 거리, 요금을 반환한다.")
    @Test
    void findShortestPath() {
        // given
        Station 판교역 = new Station(1L, "판교역");
        Station 양재역 = new Station(2L, "양재역");
        Station 도곡역 = new Station(3L, "도곡역");
        Station 강남역 = new Station(4L, "강남역");
        Station 선릉역 = new Station(5L, "선릉역");

        /*
         * 판교 -10- 양재 -10- 도곡 -10- 선릉 = 30
         * 판교 -10- 양재 -5- 강남 -5- 선릉 = 20
         * */
        Section 판교역_양재역 = new Section(1L, 판교역, 양재역, 10, 1);

        Section 양재역_도곡역 = new Section(2L, 양재역, 도곡역, 10, 2);
        Section 도곡역_선릉역 = new Section(3L, 도곡역, 선릉역, 10, 1);

        Section 양재역_강남역 = new Section(4L, 양재역, 강남역, 5, 1);
        Section 강남역_선릉역 = new Section(5L, 강남역, 선릉역, 5, 2);

        given(lineRepository.findSectionsWithSort()).willReturn(List.of(판교역_양재역, 양재역_도곡역, 도곡역_선릉역, 양재역_강남역, 강남역_선릉역));
        given(stationDao.findById(1L)).willReturn(Optional.of(new StationEntity(1L, "판교역")));
        given(stationDao.findById(5L)).willReturn(Optional.of(new StationEntity(5L, "선릉역")));

        ShortestPathRequest shortestPathRequest = new ShortestPathRequest(1L, 5L);

        // when
        ShortestPathResponse shortestPathResponse = pathService.findShortestPath(shortestPathRequest);

        // then
        List<StationResponse> path = shortestPathResponse.getPath();
        int distance = shortestPathResponse.getDistance();
        int fare = shortestPathResponse.getFare();

        assertThat(path).extracting(StationResponse::getName).containsExactly("판교역", "양재역", "강남역", "선릉역");
        assertThat(distance).isEqualTo(20);
        assertThat(fare).isEqualTo(1450);
    }
}
