package subway.application.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.domain.fare.Fare;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.path.SubwayPath;
import subway.domain.section.Sections;

@SuppressWarnings("NonAsciiCharacters")
class ExtraFarePolicyTest {
    private ExtraFarePolicy extraFarePolicy;

    @BeforeEach
    void setUp() {
        extraFarePolicy = new ExtraFarePolicy(
                new DistanceFareCalculator(),
                new LineExtraFareCalculator()
        );
    }

    @Test
    void 노선_추가_요금_계산_테스트() {
        final Line lineOne = new Line(new Sections(), new LineName("1호선"), new LineColor("파랑"), new Fare(500));
        final Line lineTwo = new Line(new Sections(), new LineName("2호선"), new LineColor("초록"), new Fare(100));
        final SubwayPath subwayPath = new SubwayPath(null, 8, Set.of(lineOne, lineTwo));

        final Fare extraFare = extraFarePolicy.calculateExtraFare(subwayPath);

        assertThat(extraFare.getFare()).isEqualTo(500);
    }

    @Test
    void 거리_추가_요금_계산_테스트() {
        final Line lineOne = new Line(new Sections(), new LineName("1호선"), new LineColor("파랑"));
        final Line lineTwo = new Line(new Sections(), new LineName("2호선"), new LineColor("초록"));
        final SubwayPath subwayPath = new SubwayPath(null, 54, Set.of(lineOne, lineTwo));

        final Fare extraFare = extraFarePolicy.calculateExtraFare(subwayPath);

        assertThat(extraFare.getFare()).isEqualTo(900);
    }

    @Test
    void 노선_거리_추가_요금_계산_테스트() {
        final Line lineOne = new Line(new Sections(), new LineName("1호선"), new LineColor("파랑"), new Fare(500));
        final Line lineTwo = new Line(new Sections(), new LineName("2호선"), new LineColor("초록"), new Fare(100));
        final SubwayPath subwayPath = new SubwayPath(null, 54, Set.of(lineOne, lineTwo));

        final Fare extraFare = extraFarePolicy.calculateExtraFare(subwayPath);

        assertThat(extraFare.getFare()).isEqualTo(500 + 900);
    }
}
