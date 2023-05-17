package subway.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.fixture.StationFixture.건대역;
import subway.fixture.StationFixture.역삼역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class RouteTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 거리가_0보다_작거나_같을때_예외(int distance) {
        // when then
        assertThatThrownBy(() -> new Route(List.of(역삼역.STATION, 건대역.STATION), distance))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("거리는 0보다 커야합니다.");
    }

    @Test
    void 경로가_null일때_예외() {
        // given
        List<Station> route = null;

        // when then
        assertThatThrownBy(() -> new Route(route, 1))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("경로에는 2개 이상의 역이 존재해야합니다.");
    }

    @Test
    void 경로에_역이_2개보다_적으면_예외() {
        // given
        List<Station> route = List.of(역삼역.STATION);

        // when then
        assertThatThrownBy(() -> new Route(route, 1))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("경로에는 2개 이상의 역이 존재해야합니다.");
    }

    @Test
    void 경로_생성() {
        List<Station> route = List.of(역삼역.STATION, 건대역.STATION);
        int distance = 1;

        // when then
        assertThatNoException().isThrownBy(() -> new Route(route, distance));
    }
}
