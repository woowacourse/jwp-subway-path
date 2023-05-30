package subway.domain.fare;

import fixtures.SectionFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.path.Path;
import subway.domain.path.SectionEdge;

import java.util.ArrayList;
import java.util.List;

import static fixtures.LineFixtures.LINE2_SURCHARGE;
import static fixtures.LineFixtures.LINE7_SURCHARGE;
import static fixtures.SectionFixtures.SECTION_잠실역_TO_건대역;
import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @Test
    @DisplayName("가격 정책들을 모두 포함한 가격을 계산한다.")
    void fareCalculateTest() {
        // given
        FareCalculator fareCalculator = new FareCalculator(List.of(new BasicFarePolicy(), new DistanceFarePolicy(), new LineSurchargeFarePolicy()));
        int basicFare = 1250;
        int distance50Surcharge = 800;
        int expectFare = basicFare + distance50Surcharge + LINE2_SURCHARGE + LINE7_SURCHARGE;
        SectionEdge line2SectionEdge = new SectionEdge(SECTION_잠실역_TO_건대역);
        SectionEdge line7SectionEdge = new SectionEdge(SectionFixtures.SECTION_온수역_TO_철산역);
        Path path = new Path(new ArrayList<>(),
                List.of(line2SectionEdge, line7SectionEdge),
                50);

        // when
        int fare = fareCalculator.calculate(path);

        // then
        assertThat(fare).isEqualTo(expectFare);
    }
}