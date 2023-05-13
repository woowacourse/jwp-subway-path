package subway.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SectionDomainTest {

    @Test
    void 상행종점이_아닌_구간을_생성한다() {
        // given
        final StationDomain upStation = new StationDomain("헤나");
        final StationDomain downStation = new StationDomain("루카");
        final Distance distance = new Distance(10);

        // expect
        assertDoesNotThrow(() -> SectionDomain.notStartFrom(upStation, downStation, distance));
    }

    @Test
    void 상행종점인_구간을_생성한다() {
        // given
        final StationDomain upStation = new StationDomain("헤나");
        final StationDomain downStation = new StationDomain("루카");
        final Distance distance = new Distance(10);

        // expect
        assertDoesNotThrow(() -> SectionDomain.startFrom(upStation, downStation, distance));
    }

    @Test
    void 상행역이_null일_경우_예외가_발생한다() {
        // given
        final StationDomain downStation = new StationDomain("루카");
        final Distance distance = new Distance(10);

        // expect
        assertThatThrownBy(() -> SectionDomain.startFrom(null, downStation, distance))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 하행역이_null일_경우_예외가_발생한다() {
        // given
        final StationDomain upStation = new StationDomain("헤나");
        final Distance distance = new Distance(10);

        // expect
        assertThatThrownBy(() -> SectionDomain.startFrom(upStation, null, distance))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 거리가_null일_경우_예외가_발생한다() {
        // given
        final StationDomain upStation = new StationDomain("헤나");
        final StationDomain downStation = new StationDomain("루카");

        // expect
        assertThatThrownBy(() -> SectionDomain.startFrom(upStation, downStation, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
