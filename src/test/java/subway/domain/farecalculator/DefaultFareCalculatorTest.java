package subway.domain.farecalculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fixture.Fixture;
import subway.domain.farecalculator.policy.additional.LineAdditionalFarePolicy;
import subway.domain.farecalculator.policy.discount.AgeDiscountPolicy;
import subway.domain.farecalculator.policy.distance.BasicFareByDistancePolicy;
import subway.dto.FareResponse;
import subway.dto.SectionResponse;

class DefaultFareCalculatorTest {
    DefaultFareCalculator fareCalculator = new DefaultFareCalculator(
            new BasicFareByDistancePolicy(),
            new LineAdditionalFarePolicy(),
            new AgeDiscountPolicy()
    );


    @Test
    @DisplayName("section과 거리를 입력받아 요금정보를 반환한다")
    void calculate() {
        //given
        final List<SectionResponse> sectionResponses = List.of(
                SectionResponse.from(Fixture.LINE_1, Fixture.STATION_1, Fixture.STATION_2),
                SectionResponse.from(Fixture.LINE_2, Fixture.STATION_2, Fixture.STATION_3),
                SectionResponse.from(Fixture.LINE_3, Fixture.STATION_3, Fixture.STATION_4)
        );
        final int distance = 13;

        //when
        final FareResponse result = fareCalculator.calculate(sectionResponses, distance, 10);

        //then
        Assertions.assertAll(
                ()->assertThat(result.getFare()).isEqualTo(950),
                ()->assertThat(result.getType()).isEqualTo("CHILD")
        );
    }

}
