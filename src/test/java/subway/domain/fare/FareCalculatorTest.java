package subway.domain.fare;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static subway.TestSource.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FareCalculatorTest {

    private final FareCalculator calculator = new FareCalculator(1250, List.of(
        new DistanceBasedFarePolicy(),
        new LineBasedFarePolicy(),
        new AgeBasedFarePolicy()
    ));

    @Nested
    class 거리에_따른_추가_금액_계산 {

        @Test
        void 추가_금액을_계산한다_9km() {
            // given
            Sections sections = getDummySectionsWithGivenDistance(9);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            assertThat(fare).isEqualTo(1250);
        }

        @Test
        void 추가_금액을_계산한다_10km() {
            // given
            Sections sections = getDummySectionsWithGivenDistance(10);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            assertThat(fare).isEqualTo(1250);
        }

        @Test
        void 추가_금액을_계산한다_12km() {
            // given
            Sections sections = getDummySectionsWithGivenDistance(12);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            assertThat(fare).isEqualTo(1350);
        }

        @Test
        void 추가_금액을_계산한다_16km() {
            // given
            Sections sections = getDummySectionsWithGivenDistance(16);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            assertThat(fare).isEqualTo(1450);
        }

        @Test
        void 추가_금액을_계산한다_50km() {
            // given
            Sections sections = getDummySectionsWithGivenDistance(50);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            assertThat(fare).isEqualTo(2050);
        }

        @Test
        void 추가_금액을_계산한다_58km() {
            // given
            Sections sections = getDummySectionsWithGivenDistance(58);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            assertThat(fare).isEqualTo(2150);
        }
    }

    @Nested
    class 노선에_따른_추가_금액_계산 {

        @Test
        void 추가_요금이_200원인_노선을_10km_이하로_사용_시() {
            // given
            Sections sections = getDummySectionsWithGivenDistanceAndAdditionalCharges(5, 200, 200);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            assertThat(fare).isEqualTo(1250 + 200);
        }

        @Test
        void 추가_요금이_200원인_노선을_12km_사용_시() {
            // given
            Sections sections = getDummySectionsWithGivenDistanceAndAdditionalCharges(6, 200, 200);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            assertThat(fare).isEqualTo(1250 + 100 + 200);
        }

        @Test
        void 추가_요금이_200원_300원인_두_노선을_총_10km_사용_시() {
            // given
            Sections sections = getDummySectionsWithGivenDistanceAndAdditionalCharges(5, 200, 300);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            assertThat(fare).isEqualTo(1250 + 300);
        }
    }

    @Nested
    class 연령에_따른_추가_요금_계산 {

        @Test
        void 어린이_이용자가_추가금_없는_10km_사용_시() {
            // given
            Sections sections = getDummySectionsWithGivenDistance(10);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).age(12).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            int discountRate = 50;
            assertThat(fare).isEqualTo(1250 - ((1250 - 350) * discountRate / 100)); // 800
        }

        @Test
        void 청소년_이용자가_추가금_없는_10km_사용_시() {
            // given
            Sections sections = getDummySectionsWithGivenDistance(10);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).age(13).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            int discountRate = 20;
            assertThat(fare).isEqualTo(1250 - ((1250 - 350) * discountRate / 100)); // 1070
        }

        @Test
        void 청소년_이용자가_300_900원의_추가금이_있는_두_노선을_총_58km_사용_시() {
            // given
            Sections sections = getDummySectionsWithGivenDistanceAndAdditionalCharges(29, 300, 900);
            FarePolicyRelatedParameters parameters = new FarePolicyRelatedParameters.Builder(sections).age(13).build();

            // when
            int fare = calculator.calculate(parameters);

            // then
            int discountRate = 20;
            assertThat(fare).isEqualTo(1250 + 900 + 900 - ((3050 - 350) * discountRate / 100)); // 2510
        }
    }

    private Sections getDummySectionsWithGivenDistance(int distance) {
        return new Sections(List.of(new Section(cheonho, jamsil, pink, distance)));
    }

    private Sections getDummySectionsWithGivenDistanceAndAdditionalCharges(int distance, int... charges) {
        return new Sections(Arrays.stream(charges)
            .mapToObj(charge -> new Section(cheonho, jamsil, new Line("name", "color", charge), distance))
            .collect(toList()));
    }
}
