package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {
    @Test
    void 구간을_생성한다() {
        // given
        Station 시작역 = new Station(1L, "잠실역");
        Station 도착역 = new Station(2L, "잠실새내역");

        // expected
        assertDoesNotThrow(() -> new Section(시작역, 도착역, 10));
    }

    @ParameterizedTest(name = "{displayName}[{index}] = ''{0}''")
    @ValueSource(ints = {0, -10})
    void 거리가_양수가_아닐_때_예외가_발생한다(int 거리) {
        // given
        Station 시작역 = new Station(1L, "잠실역");
        Station 도착역 = new Station(2L, "잠실새내역");

        // expected
        assertThatThrownBy(() -> new Section(시작역, 도착역, 거리))
                .isInstanceOf(InvalidException.class);
    }

    @Test
    void 시작역과_도착역이_같을_때_예외가_발생한다() {
        // given
        Station 시작역 = new Station(1L, "잠실역");

        // expected
        assertThatThrownBy(() -> new Section(시작역, 시작역, 10))
                .isInstanceOf(InvalidException.class);
    }

    @Test
    void 역이_구간_사이에_추가될_때_기존_구간의_거리보다_크거나_같으면_예외가_발생한다() {
        // given
        Station 시작역 = new Station(1L, "잠실역");
        Station 도착역 = new Station(2L, "잠실새내역");
        Section 기존_구간 = new Section(시작역, 도착역, 10);

        // expected
        assertThatThrownBy(() -> 기존_구간.getDividedSection(
                new Section(
                        new Station(1L, "잠실새내역"),
                        new Station(2L, "신림역"),
                        20)))
                .isInstanceOf(InvalidException.class);
    }

    @Test
    void 새로운_구간이_뒤에_추가될_때_나누어지는_구간을_리턴한다() {
        // given
        Station 시작역 = new Station(1L, "잠실역");
        Station 도착역 = new Station(2L, "잠실새내역");
        Section 기존_구간 = new Section(시작역, 도착역, 10);

        Station 새로운_시작역 = new Station(3L, "봉천역");
        Section 새로운_도착역 = new Section(새로운_시작역, 도착역, 4);

        // when
        Section 분리된_구간 = 기존_구간.getDividedSection(새로운_도착역);

        // expected
        assertAll(
                () -> assertThat(분리된_구간.getDistance()).isEqualTo(6),
                () -> assertThat(분리된_구간.getUpStation()).isEqualTo(시작역),
                () -> assertThat(분리된_구간.getDownStation()).isEqualTo(새로운_시작역)
        );
    }

    @Test
    @DisplayName("새로운 구간이 앞에 추가될 때 나누어지는 구간을 리턴한다.")
    void getDividedSectionFront() {
        // given
        Station 시작역 = new Station(1L, "잠실역");
        Station 도착역 = new Station(2L, "잠실새내역");
        Section 기존_구간 = new Section(시작역, 도착역, 10);

        Station 새로운_도착역 = new Station(3L, "봉천역");
        Section 새로운_구간 = new Section(시작역, 새로운_도착역, 4);

        // when
        Section 분리된_구간 = 기존_구간.getDividedSection(새로운_구간);

        // expected
        assertAll(
                () -> assertThat(분리된_구간.getDistance()).isEqualTo(6),
                () -> assertThat(분리된_구간.getUpStation()).isEqualTo(새로운_도착역),
                () -> assertThat(분리된_구간.getDownStation()).isEqualTo(도착역)
        );
    }
}
