package subway.domain.fare;

import static org.assertj.core.api.Assertions.*;
import static subway.TestSource.*;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import subway.domain.Section;
import subway.domain.Sections;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FareCalculatorTest {

    private final FareCalculator calculator = new FareCalculator();

    @Test
    void 추가_금액을_계산한다_9km() {
        // given
        Sections sections = getDummySectionsWithGivenDistance(9);

        // when
        int additionalFare = calculator.calculate(sections);

        // then
        assertThat(additionalFare).isEqualTo(1250);
    }

    @Test
    void 추가_금액을_계산한다_10km() {
        // given
        Sections sections = getDummySectionsWithGivenDistance(10);

        // when
        int additionalFare = calculator.calculate(sections);

        // then
        assertThat(additionalFare).isEqualTo(1250);
    }

    @Test
    void 추가_금액을_계산한다_12km() {
        // given
        Sections sections = getDummySectionsWithGivenDistance(12);

        // when
        int additionalFare = calculator.calculate(sections);

        // then
        assertThat(additionalFare).isEqualTo(1350);
    }

    @Test
    void 추가_금액을_계산한다_16km() {
        // given
        Sections sections = getDummySectionsWithGivenDistance(16);

        // when
        int additionalFare = calculator.calculate(sections);

        // then
        assertThat(additionalFare).isEqualTo(1450);
    }

    @Test
    void 추가_금액을_계산한다_50km() {
        // given
        Sections sections = getDummySectionsWithGivenDistance(50);

        // when
        int additionalFare = calculator.calculate(sections);

        // then
        assertThat(additionalFare).isEqualTo(2050);
    }

    @Test
    void 추가_금액을_계산한다_58km() {
        // given
        Sections sections = getDummySectionsWithGivenDistance(58);

        // when
        int additionalFare = calculator.calculate(sections);

        // then
        assertThat(additionalFare).isEqualTo(2150);
    }

    private Sections getDummySectionsWithGivenDistance(int distance) {
        return new Sections(List.of(new Section(cheonho, jamsil, pink, distance)));
    }
}
