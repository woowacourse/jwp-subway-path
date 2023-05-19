package subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.line.domain.fixture.StationFixture.선릉;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역4;
import static subway.line.domain.fixture.StationFixture.역5;
import static subway.line.domain.fixture.StationFixture.역6;
import static subway.line.domain.fixture.StationFixture.잠실;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.domain.Station;
import subway.line.exception.line.LineException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Path 은(는)")
class PathTest {

    private final Path path = new Path(
            new Line("2호선", 0,
                    new Section(선릉, 역2, 1),
                    new Section(역2, 잠실, 7)
            ),
            new Line("1호선", 0,
                    new Section(선릉, 역4, 10),
                    new Section(역4, 역5, 5)
            ),
            new Line("4호선", 0,
                    new Section(역6, 역5, 10)
            )
    );

    @Test
    void 각_노선들의_길이의_총합을_구할_수_있다() {
        // when
        final int totalDistance = path.totalDistance();

        // then
        assertThat(totalDistance).isEqualTo(33);
    }

    @Nested
    class 주어진_역으로_시작해서_목적지까지_연속적인_노선들을_구할_때 {

        @Test
        void 주어진_역으로_시작하는_경로를_구한다() {
            // given
            final Path result = path.continuousPathWithStartStation(잠실);

            // when & then
            assertThat(result.lines())
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
        void 주어진_역이_시작점이_될_수_없으면_예외() {
            // when
            final List<Station> noneStartStations = List.of(역2, 역4, 역5,
                    역6);
            for (final Station noneStartStation : noneStartStations) {
                final String message = assertThrows(LineException.class, () ->
                        path.continuousPathWithStartStation(noneStartStation)
                ).getMessage();

                // then
                assertThat(message).contains("경로가 주어진 역으로 시작할 수 없습니다.");
            }
        }

        @Test
        void 노선들이_연속적으로_연결될_수_없다면_예외() {
            // when
            final String message = assertThrows(LineException.class, () ->
                    path.continuousPathWithStartStation(선릉)
            ).getMessage();

            // then
            assertThat(message).contains("노선들이 연결될 수 없습니다");
        }
    }
}
