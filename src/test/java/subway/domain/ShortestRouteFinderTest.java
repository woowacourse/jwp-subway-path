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
    }
}
