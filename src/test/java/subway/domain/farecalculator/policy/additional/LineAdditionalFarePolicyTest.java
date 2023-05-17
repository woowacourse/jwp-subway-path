package subway.domain.farecalculator.policy.additional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fixture.Fixture;
import subway.dto.SectionResponse;

class LineAdditionalFarePolicyTest {

    LineAdditionalFarePolicy lineAdditionalFarePolicy = new LineAdditionalFarePolicy();

    @Test
    @DisplayName("SectionResponse 에 담겨있는 AdditionalFare 중 가장 큰값을 반환한다.")
    void calculateAdditionalFare() {
        //given
        final List<SectionResponse> sections = List.of(
                SectionResponse.from(Fixture.LINE_1, Fixture.STATION_1, Fixture.STATION_2),
                SectionResponse.from(Fixture.LINE_2, Fixture.STATION_2, Fixture.STATION_3),
                SectionResponse.from(Fixture.LINE_3, Fixture.STATION_3, Fixture.STATION_4)
        );

        //when
        final int result = lineAdditionalFarePolicy.calculateAdditionalFare(sections);

        //then
        assertThat(result).isEqualTo(900);
    }
}
