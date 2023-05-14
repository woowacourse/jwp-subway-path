package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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

    @ParameterizedTest
    @MethodSource("isBaseStationExistDummy")
    void 입력된_구간의_역_중에_하나라도_같은_역이_존재하는지_확인한다(final SectionDomain otherSection, final boolean expect) {
        // given
        final SectionDomain section = SectionDomain.notStartFrom(
                new StationDomain("잠실"), new StationDomain("잠실나루"), new Distance(10));

        // when
        final boolean isBaseStationExist = section.isBaseStationExist(otherSection);

        // then
        assertThat(isBaseStationExist).isEqualTo(expect);
    }

    static Stream<Arguments> isBaseStationExistDummy() {
        return Stream.of(
                Arguments.arguments(SectionDomain.notStartFrom(
                        new StationDomain("잠실"), new StationDomain("잠실나루"), new Distance(10)), true),
                Arguments.arguments(SectionDomain.notStartFrom(
                        new StationDomain("잠실새내"), new StationDomain("잠실나루"), new Distance(10)), true),
                Arguments.arguments(SectionDomain.notStartFrom(
                        new StationDomain("잠실"), new StationDomain("잠실새내"), new Distance(10)), true),
                Arguments.arguments(SectionDomain.notStartFrom(
                        new StationDomain("잠실새내"), new StationDomain("강변"), new Distance(10)), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isSameUpStationByDummy")
    void 입력된_역이_구간의_상행역과_같은지_확인한다(final StationDomain station, final boolean expect) {
        // given
        final SectionDomain section = SectionDomain.notStartFrom(
                new StationDomain("잠실"), new StationDomain("잠실나루"), new Distance(10));

        // when
        final boolean isSameUpStation = section.isSameUpStationBy(station);

        // then
        assertThat(isSameUpStation).isEqualTo(expect);
    }

    static Stream<Arguments> isSameUpStationByDummy() {
        return Stream.of(
                Arguments.arguments(new StationDomain("잠실"), true),
                Arguments.arguments(new StationDomain("잠실나루"), false),
                Arguments.arguments(new StationDomain("잠실새내"), false),
                Arguments.arguments(new StationDomain("강변"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isSameDownStationByDummy")
    void 입력된_역이_구간의_하행역과_같은지_확인한다(final StationDomain station, final boolean expect) {
        // given
        final SectionDomain section = SectionDomain.notStartFrom(
                new StationDomain("잠실"), new StationDomain("잠실나루"), new Distance(10));

        // when
        final boolean isSameDownStation = section.isSameDownStationBy(station);

        // then
        assertThat(isSameDownStation).isEqualTo(expect);
    }

    static Stream<Arguments> isSameDownStationByDummy() {
        return Stream.of(
                Arguments.arguments(new StationDomain("잠실나루"), true),
                Arguments.arguments(new StationDomain("잠실"), false),
                Arguments.arguments(new StationDomain("잠실새내"), false),
                Arguments.arguments(new StationDomain("강변"), false)
        );
    }
}
