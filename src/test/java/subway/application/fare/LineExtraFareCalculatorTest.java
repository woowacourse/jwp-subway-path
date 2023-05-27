package subway.application.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.domain.fare.Fare;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.section.Sections;

@SuppressWarnings("NonAsciiCharacters")
class LineExtraFareCalculatorTest {

    private LineExtraFareCalculator lineExtraFareCalculator = new LineExtraFareCalculator();

    @BeforeEach
    void setUp() {
        lineExtraFareCalculator = new LineExtraFareCalculator();
    }

    @Test
    void 노선_추가_요금_계산_테스트() {
        final Line lineOne = new Line(new Sections(), new LineName("1호선"), new LineColor("파랑"), new Fare(500));
        final Line lineTwo = new Line(new Sections(), new LineName("2호선"), new LineColor("초록"), new Fare(400));
        final Line lineThree = new Line(new Sections(), new LineName("3호선"), new LineColor("주황"), new Fare(100));
        final Set<Line> lines = Set.of(lineOne, lineTwo, lineThree);

        final Fare extraFare = lineExtraFareCalculator.calculateLineExtraFare(lines);

        assertThat(extraFare).isEqualTo(new Fare(500));
    }

}
