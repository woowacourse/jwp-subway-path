package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.exception.NoSuchRouteException;
import subway.repository.LineRepository;

import java.util.List;

import static subway.fixture.StationFixture.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(ShortestRouteFinder.class)
class ShortestRouteFinderTest {

    @Autowired
    private ShortestRouteFinder routeFinder;

    @MockBean
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        Line line = new Line("2호선", "GREEN");
        line.addSection(new Section(역삼역.STATION, 삼성역.STATION, 5));
        line.addSection(new Section(삼성역.STATION, 잠실역.STATION, 5));
        BDDMockito.given(lineRepository.findAll())
                .willReturn(List.of(line));
    }

    @Nested
    class 경로_탐색_시_ {

        @Test
        void 출발역이_없으면_예외() {
            //when & then
            Assertions.assertThatThrownBy(() -> routeFinder.findRoute(건대역.STATION, 잠실역.STATION))
                    .isInstanceOf(NoSuchRouteException.class)
                    .hasMessage("존재하지 않는 경로입니다." +
                            "from : " + 건대역.STATION +
                            "to : " + 잠실역.STATION);
        }

        @Test
        void 도착역이_없으면_예외() {
            //when & then
            Assertions.assertThatThrownBy(() -> routeFinder.findRoute(잠실역.STATION, 건대역.STATION))
                    .isInstanceOf(NoSuchRouteException.class)
                    .hasMessage("존재하지 않는 경로입니다." +
                            "from : " + 잠실역.STATION +
                            "to : " + 건대역.STATION);
        }

        /*
        강남 - 5km - 종합운동장
        강남 - 1km - 신논현역 - 1km - 종합운동장
         */
        @Test
        void 최단경로를_구한다() {
            Line 이호선 = new Line("2호선", "GREEN");
            이호선.addSection(new Section(강남역.STATION, 종합운동장역.STATION, 5));

            Line 신분당선 = new Line("신분당선", "RED");
            신분당선.addSection(new Section(강남역.STATION, 신논현역.STATION, 1));

            Line 구호선 = new Line("9호선", "BROWN");
            구호선.addSection(new Section(신논현역.STATION, 종합운동장역.STATION, 1));

            BDDMockito.given(lineRepository.findAll())
                    .willReturn(List.of(이호선, 신분당선, 구호선));

            List<Station> route = routeFinder.findRoute(강남역.STATION, 종합운동장역.STATION);

            Assertions.assertThat(route)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(List.of(강남역.STATION, 신논현역.STATION, 종합운동장역.STATION));
        }
    }
}
