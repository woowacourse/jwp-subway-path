package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.domain.LinkedRoute.of;
import static subway.domain.fixture.StationFixture.선릉;
import static subway.domain.fixture.StationFixture.역1;
import static subway.domain.fixture.StationFixture.역2;
import static subway.domain.fixture.StationFixture.역4;
import static subway.domain.fixture.StationFixture.역5;
import static subway.domain.fixture.StationFixture.역6;
import static subway.domain.fixture.StationFixture.잠실;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.exception.line.LineException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LinkedRoute 은(는)")
class LinkedRouteTest {

    private final List<Line> lines = new ArrayList<>(List.of(
            new Line("2호선", new Sections(List.of(
                    new Section(선릉, 역2, 1),
                    new Section(역2, 잠실, 7)
            ))),
            new Line("1호선", new Sections(List.of(
                    new Section(선릉, 역4, 10),
                    new Section(역4, 역5, 5)
            ))),
            new Line("4호선", new Sections(List.of(
                    new Section(역6, 역5, 10)
            )))
    ));

    @Test
    void 역들이_연결되어_생성된다() {
        // given
        final LinkedRoute route = of(잠실, lines);

        // when & then
        assertThat(route.lines())
                .flatMap(Line::sections)
                .containsExactly(
                        new Section(잠실, 역2, 7),
                        new Section(역2, 선릉, 1),
                        new Section(선릉, 역4, 10),
                        new Section(역4, 역5, 5),
                        new Section(역5, 역6, 10)
                );
    }

    @Test
    void 경로가_주어진_역으로_시작하지_않는다면_예외() {
        // when
        final List<Station> noneStartStations = List.of(역2, 역4, 역5, 역6);
        for (final Station noneStartStation : noneStartStations) {
            final String message = assertThrows(LineException.class, () ->
                    LinkedRoute.of(noneStartStation, lines)
            ).getMessage();

            // then
            assertThat(message).contains("경로가 주어진 역으로 시작할 수 없습니다.");
        }
    }

    @Test
    void 역들이_연결될_수_없다면_예외() {
        // when
        final String message = assertThrows(LineException.class, () ->
                LinkedRoute.of(선릉, lines)
        ).getMessage();

        // then
        assertThat(message).contains("노선들이 연결될 수 없습니다");
    }

    @Test
    void 각_노선들의_길이의_총합을_구할_수_있다() {
        // given
        final LinkedRoute linkedRoute = of(잠실, lines);

        // when
        final int totalDistance = linkedRoute.totalDistance();

        // then
        assertThat(totalDistance).isEqualTo(33);
    }

    @Test
    void 비었는지_확인한다() {
        // given
        final LinkedRoute of = of(역1, new ArrayList<>());

        // when & then
        assertThat(of.isEmpty()).isTrue();
    }
}
