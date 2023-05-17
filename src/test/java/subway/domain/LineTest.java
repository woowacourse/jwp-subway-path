package subway.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.section.Section;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    private static final String NAME = "신분당선";
    private static final String COLOR = "bg-red-900";

    @Nested
    class LineNameTest {
        @ValueSource(strings = {"가1", "가나다라마바사아자"})
        @ParameterizedTest
        void 정상적인_값이_이름으로_들어오면_노선을_생성한다(final String input) {
            assertDoesNotThrow(() -> Line.of(input, COLOR));
        }

        @ValueSource(strings = {"test", "1q2q", "가나다a"})
        @ParameterizedTest
        void 한글과_숫자가_아닌_값이_이름으로_들어오면_예외가_발생한다(final String input) {
            assertThatThrownBy(() -> Line.of(input, COLOR))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("노선 이름은 한글과 숫자만 가능합니다");
        }

        @ValueSource(strings = {"가", "가나다라마바사123"})
        @ParameterizedTest
        void 길이가_2_이상_9_이하가_아닌_값이_이름으로_들어오면_예외가_발생한다(final String input) {
            assertThatThrownBy(() -> Line.of(input, COLOR))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("노선 이름은 2글자 ~ 9글자만 가능합니다");
        }
    }

    @Nested
    class LineColorTest {

        @Test
        void 정상적인_값이_색깔로_들어오면_노선을_생성한다() {
            final String color = "bg-red-900";

            assertDoesNotThrow(() -> Line.of(NAME, color));
        }

        @Test
        void 비정상적인_값이_들어오면_예외를_발생한다() {
            final String color = "bg-ab-9000";

            assertThatThrownBy(() -> Line.of(NAME, color))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("노선 색깔은 tailwindcss 형식만 가능합니다");
        }
    }

    @Nested
    class LineAddTest {

        private Line line;
        private Station stationOne;
        private Station stationTwo;
        private Station stationThree;
        private Distance distanceOneTwo;
        private Distance distanceTwoThree;

        @BeforeEach
        void setUp() {
            line = Line.of(NAME, COLOR);

            stationOne = Station.from("잠실역");
            stationTwo = Station.from("선릉역");
            distanceOneTwo = Distance.from(10);

            stationThree = Station.from("잠실새내역");
            distanceTwoThree = Distance.from(5);
        }

        @Test
        void 노선에_등록된_역이_없을_때_두_개의_역을_등록해야_한다() {
            final Station stationOne = Station.from("잠실역");
            final Station stationTwo = Station.from("선릉역");
            final Distance distanceOneTwo = Distance.from(10);

            SoftAssertions.assertSoftly(softAssertions -> {
                assertDoesNotThrow(() -> line.addSection(Section.of(stationOne, stationTwo, distanceOneTwo)));
            });
        }

        @Test
        void 성공적으로_종점을_추가한다() {
            line.addSection(Section.of(stationOne, stationTwo, distanceOneTwo));

            final Station station = Station.from("서울역");
            final Distance distance = Distance.from(5);

            line.addSection(Section.of(stationTwo, station, distance));
        }

        @Test
        void 역과_역_사이에_새로운_역을_추가한다() {
            line.addSection(Section.of(stationOne, stationTwo, distanceOneTwo));

            final Station station = Station.from("서울역");
            final Distance distance = Distance.from(3);

            line.addSection(Section.of(stationOne, station, distance));
        }

        @Test
        void 역과_역_사이에_새로운_역을_추가할_때_원래_역_사이의_거리_이상의_거리를_추가할_경우_예외가_발생한다() {
            line.addSection(Section.of(stationOne, stationTwo, distanceOneTwo));

            final Station station = Station.from("서울역");
            final Distance distance = Distance.from(15);

            assertThatThrownBy(() -> line.addSection(Section.of(stationOne, station, distance)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록하려는 구간의 길이는 원본 구간의 길이 미만이어야 합니다.");
        }
    }

    @Nested
    class LineShowAllTest {

        private Line line;

        @BeforeEach
        void setUp() {
            line = Line.of(NAME, COLOR);

            final Station stationOne = Station.from("잠실역");
            final Station stationTwo = Station.from("선릉역");
            final Distance distanceOneTwo = Distance.from(10);
            final Station stationThree = Station.from("잠실새내역");
            final Distance distanceTwoThree = Distance.from(5);
            final Station stationFour = Station.from("사당역");
            final Distance distanceThreeFour = Distance.from(5);
            final Station stationFive = Station.from("신논현역");
            final Distance distanceFourFive = Distance.from(5);

            line.addSection(Section.of(stationOne, stationTwo, distanceOneTwo));
            line.addSection(Section.of(stationTwo, stationThree, distanceTwoThree));
            line.addSection(Section.of(stationThree, stationFour, distanceThreeFour));
            line.addSection(Section.of(stationFour, stationFive, distanceFourFive));
        }

        @Test
        void 노선에_저장된_역을_조회할_때_상행역부터_하행역으로_가져온다() {
            List<Station> stations = line.findStationsByOrdered();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(stations.get(0).getName()).isEqualTo("잠실역");
                softAssertions.assertThat(stations.get(1).getName()).isEqualTo("선릉역");
                softAssertions.assertThat(stations.get(2).getName()).isEqualTo("잠실새내역");
                softAssertions.assertThat(stations.get(3).getName()).isEqualTo("사당역");
                softAssertions.assertThat(stations.get(4).getName()).isEqualTo("신논현역");
            });
        }
    }
}
