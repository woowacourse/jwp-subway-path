package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.vo.Distance;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static org.junit.jupiter.params.provider.Arguments.*;

@DisplayNameGeneration(ReplaceUnderscores.class)
class SectionTest {
    private static final Distance 거리10 = Distance.from(10);

    @Test
    void 상행종점이_아닌_구간을_생성한다() {
        // given
        final Station 헤나역 = new Station("헤나");
        final Station 루카역 = new Station("루카");

        // expect
        assertDoesNotThrow(() -> new Section(거리10, false, 헤나역, 루카역));
    }

    @Test
    void 상행종점인_구간을_생성한다() {
        // given
        final Station 헤나역 = new Station("헤나");
        final Station 루카역 = new Station("루카");
        final Distance 거리10 = Distance.from(10);

        // expect
        assertDoesNotThrow(() -> new Section(거리10, true, 헤나역, 루카역));
    }

    @Test
    void 상행역이_null일_경우_예외가_발생한다() {
        // given
        final Station 루카역 = new Station("루카");
        final Distance 거리10 = Distance.from(10);

        // expect
        assertThatThrownBy(() -> new Section(거리10, true, null, 루카역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 하행역이_null일_경우_예외가_발생한다() {
        // given
        final Station 헤나역 = new Station("헤나");
        final Distance 거리10 = Distance.from(10);

        // expect
        assertThatThrownBy(() -> new Section(거리10, true, 헤나역, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 거리가_null일_경우_예외가_발생한다() {
        // given
        final Station 헤나역 = new Station("헤나");
        final Station 루카역 = new Station("루카");

        // expect
        assertThatThrownBy(() -> new Section(null, true, 헤나역, 루카역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("isBaseStationExistDummy")
    void 입력된_구간의_역_중에_하나라도_같은_역이_존재하는지_확인한다(final Section 새로운_구간, final boolean expect) {
        // given
        final Section 기존_구간 = new Section(거리10, false,
                new Station("잠실"), new Station("잠실나루"));

        // when
        final boolean 겹치는_역이_존재하는가 = 기존_구간.isBaseStationExist(새로운_구간);

        // then
        assertThat(겹치는_역이_존재하는가).isEqualTo(expect);
    }

    static Stream<Arguments> isBaseStationExistDummy() {
        return Stream.of(
                arguments(new Section(거리10, false,
                        new Station("잠실"), new Station("잠실나루")), true),
                arguments(new Section(거리10, false,
                        new Station("잠실새내"), new Station("잠실나루")), true),
                arguments(new Section(거리10, false,
                        new Station("잠실"), new Station("잠실새내")), true),
                arguments(new Section(거리10, false,
                        new Station("잠실새내"), new Station("강변")), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isSameUpStationByDummy")
    void 입력된_역이_구간의_상행역과_같은지_확인한다(final Station 역, final boolean expect) {
        // given
        final Section 기존_구간 = new Section(거리10, false,
                new Station("잠실"), new Station("잠실나루"));

        // when
        final boolean 상행역_인가 = 기존_구간.isSameUpStationBy(역);

        // then
        assertThat(상행역_인가).isEqualTo(expect);
    }

    static Stream<Arguments> isSameUpStationByDummy() {
        return Stream.of(
                arguments(new Station("잠실"), true),
                arguments(new Station("잠실나루"), false),
                arguments(new Station("잠실새내"), false),
                arguments(new Station("강변"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isSameDownStationByDummy")
    void 입력된_역이_구간의_하행역과_같은지_확인한다(final Station 역, final boolean expect) {
        // given
        final Section 기존_구간 = new Section(거리10, false,
                new Station("잠실"), new Station("잠실나루"));

        // when
        final boolean 하행역_인가 = 기존_구간.isSameDownStationBy(역);

        // then
        assertThat(하행역_인가).isEqualTo(expect);
    }

    static Stream<Arguments> isSameDownStationByDummy() {
        return Stream.of(
                arguments(new Station("잠실나루"), true),
                arguments(new Station("잠실"), false),
                arguments(new Station("잠실새내"), false),
                arguments(new Station("강변"), false)
        );
    }

    @Test
    void 기존_구간의_상행역과_새로운_구간의_하행역이_같은_경우_구간을_연결한다() {
        // given
        final Station 기존_상행역 = new Station("기존_상행역");
        final Station 기존_하행역 = new Station("기존_하행역");

        final Station 새로운_상행역 = new Station("새로운_상행역");
        final Distance 거리10 = Distance.from(10);
        final Distance 거리5 = Distance.from(5);

        final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);
        final Section 새로운_구간 = new Section(거리5, false, 새로운_상행역, 기존_상행역);

        // when
        final List<Section> 분리된_두_구간 = 기존_구간.divide(새로운_구간);

        // then
        assertThat(분리된_두_구간)
                .contains(
                        new Section(거리5, true, 새로운_상행역, 기존_상행역),
                        new Section(거리10, false, 기존_상행역, 기존_하행역)
                );
    }

    @Test
    void 기존_구간의_하행역과_새로운_구간의_상행역이_같은_경우_구간을_연결한다() {
        final Station 기존_상행역 = new Station("기존_상행역");
        final Station 기존_하행역 = new Station("기존_하행역");

        final Station 새로운_하행역 = new Station("새로운_하행역");
        final Distance 거리10 = Distance.from(10);
        final Distance 거리5 = Distance.from(5);

        final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);
        final Section 새로운_구간 = new Section(거리5, false, 기존_하행역, 새로운_하행역);

        // when
        final List<Section> 분리된_두_구간 = 기존_구간.divide(새로운_구간);

        // then
        assertThat(분리된_두_구간)
                .contains(
                        new Section(거리10, true, 기존_상행역, 기존_하행역),
                        new Section(거리5, false, 기존_하행역, 새로운_하행역)
                );
    }

    @Test
    void 기존_구간의_상행역과_새로운_구간의_상행역이_같은_경우_구간을_분리한다() {
        final Station 기존_상행역 = new Station("기존_상행역");
        final Station 기존_하행역 = new Station("기존_하행역");

        final Station 새로운_하행역 = new Station("새로운_하행역");
        final Distance 거리10 = Distance.from(10);
        final Distance 거리5 = Distance.from(5);

        final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);
        final Section 새로운_구간 = new Section(거리5, false, 기존_상행역, 새로운_하행역);

        // when
        final List<Section> 분리된_두_구간 = 기존_구간.divide(새로운_구간);

        // then
        assertThat(분리된_두_구간)
                .contains(
                        new Section(거리5, true, 기존_상행역, 새로운_하행역),
                        new Section(거리5, false, 새로운_하행역, 기존_하행역)
                );
    }

    @Test
    void 기존_구간의_하행역과_새로운_구간의_하행역이_같은_경우_구간을_분리한다() {
        final Station 기존_상행역 = new Station("기존_상행역");
        final Station 기존_하행역 = new Station("기존_하행역");

        final Station 새로운_상행역 = new Station("새로운_상행역");
        final Distance 거리10 = Distance.from(10);
        final Distance 거리5 = Distance.from(5);

        final Section 기존_구간 = new Section(거리10, true, 기존_상행역, 기존_하행역);
        final Section 새로운_구간 = new Section(거리5, false, 새로운_상행역, 기존_하행역);

        // when
        final List<Section> 분리된_두_구간 = 기존_구간.divide(새로운_구간);

        // then
        assertThat(분리된_두_구간)
                .contains(
                        new Section(거리5, true, 기존_상행역, 새로운_상행역),
                        new Section(거리5, false, 새로운_상행역, 기존_하행역)
                );
    }
}
