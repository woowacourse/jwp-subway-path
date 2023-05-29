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
class FareCalculatorTest {

    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        final ExtraFarePolicy extraFarePolicy = new ExtraFarePolicy(
                new DistanceFareCalculator(),
                new LineExtraFareCalculator()
        );

        final AgeDiscountPolicy ageDiscountPolicy = new AgeDiscountPolicy();

        fareCalculator = new FareCalculator(extraFarePolicy, ageDiscountPolicy);
    }

    @Test
    void 요금_계산_테스트() {
        final Line lineOne = new Line(new Sections(), new LineName("1호선"), new LineColor("파랑"), new Fare(500));
        final Line lineTwo = new Line(new Sections(), new LineName("2호선"), new LineColor("초록"), new Fare(100));
        final SubwayPath subwayPath = new SubwayPath(null, 54, Set.of(lineOne, lineTwo));
        final int age = 14;

        final Fare fare = fareCalculator.calculatePathFare(subwayPath, age);

        assertThat(fare.getFare()).isEqualTo(1840); // (1250 + (900 + 500) - 350) * 0.8 = 1840
    }

    @Test
    void 요금_계산_테스트_2() {
        final Line lineOne = new Line(new Sections(), new LineName("1호선"), new LineColor("파랑"));
        final Line lineTwo = new Line(new Sections(), new LineName("2호선"), new LineColor("초록"));
        final SubwayPath subwayPath = new SubwayPath(null, 1, Set.of(lineOne, lineTwo));
        final int age = 7;

        final Fare fare = fareCalculator.calculatePathFare(subwayPath, age);

        assertThat(fare.getFare()).isEqualTo(450); // (1250 - 350) * 0.5 = 450
    }
}
