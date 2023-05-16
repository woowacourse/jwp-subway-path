package subway.domain.section;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.exception.ErrorCode.SECTION_DISTANCE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.BadRequestException;

class SectionDistanceTest {

    @ParameterizedTest(name = "거리가 1 이상 50 이하면 정상 생성된다.")
    @ValueSource(ints = {1, 50})
    void station_name_success_test(final int distance) {
        final SectionDistance sectionDistance = assertDoesNotThrow(() -> SectionDistance.create(distance));
        assertThat(sectionDistance)
            .extracting("distance")
            .isEqualTo(distance);
    }

    @ParameterizedTest(name = "거리가 1 미만 50 초과면 예외가 발생한다.")
    @ValueSource(ints = {0, 51})
    void station_name_fail_test(final int distance) {
        assertThatThrownBy(() -> SectionDistance.create(distance))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(SECTION_DISTANCE);
    }

    @Test
    @DisplayName("거리가 0인 구간 거리 객체를 생성한다.")
    void zero() {
        // when
        final SectionDistance sectionDistance = SectionDistance.zero();

        // then
        assertThat(sectionDistance.distance())
            .isEqualTo(0);
    }

    @Test
    @DisplayName("구간의 합을 구한다.")
    void add() {
        // given
        final SectionDistance zero = SectionDistance.zero();

        // when
        final SectionDistance result = zero.add(SectionDistance.create(10));

        // then
        assertThat(result.distance())
            .isEqualTo(10);
    }

    @Test
    @DisplayName("구간의 차를 구한다.")
    void subtract() {
        // given
        final SectionDistance ten = SectionDistance.create(10);

        // when
        final SectionDistance result = ten.subtract(SectionDistance.create(3));

        // then
        assertThat(result.distance())
            .isEqualTo(7);
    }

    @ParameterizedTest(name = "주어진 구간이 더 크거나 같으면 true를, 아니면 false를 반환한다.")
    @CsvSource(value = {"5:false", "7:true", "10:true"}, delimiter = ':')
    void isGreaterAndEqualsThan(final int distance, final boolean expected) {
        // given
        final SectionDistance sectionDistance = SectionDistance.create(distance);

        // when
        final boolean actual = sectionDistance.isGreaterAndEqualsThan(SectionDistance.create(7));

        // then
        assertThat(actual)
            .isSameAs(expected);
    }
}
