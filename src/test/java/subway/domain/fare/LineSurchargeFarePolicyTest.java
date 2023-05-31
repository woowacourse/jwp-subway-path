package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.path.Path;
import subway.domain.path.SectionEdge;

import java.util.ArrayList;
import java.util.List;

import static fixtures.LineFixtures.LINE2_SURCHARGE;
import static fixtures.SectionFixtures.SECTION_강변역_TO_건대역;
import static org.assertj.core.api.Assertions.assertThat;

class LineSurchargeFarePolicyTest {

    @Test
    @DisplayName("노선별 추가 요금을 계산한다.")
    void calculateLineSurchargeFareTest() {
        // given
        FarePolicy farePolicy = new LineSurchargeFarePolicy();
        int expectFare = LINE2_SURCHARGE;
        Path path = new Path(new ArrayList<>(), List.of(new SectionEdge(SECTION_강변역_TO_건대역)), 100);

        // when
        int fare = farePolicy.calculateFare(path);

        // then
        assertThat(fare).isEqualTo(expectFare);
    }

}