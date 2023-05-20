package subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.line.domain.fixture.StationFixture.건대입구;
import static subway.line.domain.fixture.StationFixture.선릉;
import static subway.line.domain.fixture.StationFixture.없는역;
import static subway.line.domain.fixture.StationFixture.없는역2;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.잠실;
import static subway.line.domain.fixture.StationFixture.잠실나루;
import static subway.line.exception.line.LineExceptionType.NON_POSITIVE_DISTANCE;
import static subway.line.exception.line.LineExceptionType.UP_AND_DOWN_STATION_IS_SAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.common.exception.BaseExceptionType;
import subway.line.exception.line.LineException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Section 은(는)")
class SectionTest {

    @Test
    void 정보가_같은_두_역은_동등하다() {
        // given
        final Section section1 = new Section(역1, 역2, 10);
        final Section section2 = new Section(역1, 역2, 10);
        final Section section3 = new Section(역2, 역1, 10);
        final Section section4 = new Section(역1, 역2, 1);

        // when & then
        assertAll(
                () -> assertThat(section1).isEqualTo(section2),
                () -> assertThat(section1).isNotEqualTo(section3),
                () -> assertThat(section1).isNotEqualTo(section4)
        );
    }

    @Test
    void 상대적으로_상행_구간인지_알_수_있다() {
        // given
        final Section up = new Section(잠실, 선릉, 10);
        final Section down = new Section(선릉, 건대입구, 2);

        // when & then
        assertThat(up.isUpThan(down)).isTrue();
        assertThat(down.isUpThan(up)).isFalse();
    }

    @Test
    void 상대적으로_하행_구간인지_알_수_있다() {
        // given
        final Section up = new Section(잠실, 선릉, 10);
        final Section down = new Section(선릉, 건대입구, 2);

        // when & then
        assertThat(up.isDownThan(down)).isFalse();
        assertThat(down.isDownThan(up)).isTrue();
    }

    @Test
    void 상행역_혹은_하행역이_동일한지_판단한다() {
        // given
        final Section section1 = new Section(잠실, 선릉, 10);
        final Section section2 = new Section(잠실, 역1, 18);
        final Section section3 = new Section(역2, 선릉, 12);

        // when & then
        assertAll(
                () -> assertThat(section1.hasSameUpOrDownStation(section2)).isTrue(),
                () -> assertThat(section1.hasSameUpOrDownStation(section3)).isTrue(),
                () -> assertThat(section3.hasSameUpOrDownStation(section2)).isFalse()
        );
    }

    @Test
    void 특정_역을_포함하는지_확인한다() {
        // given
        final Section section = new Section(역1, 역2, 10);

        // when & then
        assertAll(
                () -> assertThat(section.contain(역1)).isTrue(),
                () -> assertThat(section.contain(역2)).isTrue(),
                () -> assertThat(section.contain(역3)).isFalse()
        );
    }

    @Test
    void 두_구간의_역이_모두_동일한지_확인한다() {
        // given
        final Section section1 = new Section(역1, 역2, 10);
        final Section section2 = new Section(역2, 역1, 5);
        final Section section3 = new Section(역3, 역2, 10);

        // when & then
        assertAll(
                () -> assertThat(section1.containsAllStation(section2)).isTrue(),
                () -> assertThat(section2.containsAllStation(section1)).isTrue(),
                () -> assertThat(section1.containsAllStation(section3)).isFalse(),
                () -> assertThat(section2.containsAllStation(section3)).isFalse()
        );
    }

    @Test
    void 순서를_뒤바꾼다() {
        // given
        final Section section = new Section(역1, 역2, 10);

        // when
        final Section reverse = section.reverse();

        // then
        assertAll(
                () -> assertThat(reverse.up()).isEqualTo(역2),
                () -> assertThat(reverse.down()).isEqualTo(역1),
                () -> assertThat(reverse.distance()).isEqualTo(10)
        );
    }

    @Nested
    class 구간_생성_시 {

        @Test
        void 구간_거리가_양수가_아니면_예외() {
            // given
            final Station 출발역 = new Station("출발역");
            final Station 종착역 = new Station("종착역");

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    new Section(출발역, 종착역, 0)
            ).exceptionType();
            assertThat(exceptionType).isEqualTo(NON_POSITIVE_DISTANCE);
        }

        @Test
        void 시작점과_종점이_동일하면_예외() {
            // given
            final Station 출발역 = new Station("출발역");

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    new Section(출발역, 출발역, 1)
            ).exceptionType();
            assertThat(exceptionType).isEqualTo(UP_AND_DOWN_STATION_IS_SAME);
        }
    }

    @Nested
    class 두_구간의_합을_구할_때 {

        @Test
        void 합쳐진_구간의_길이는_두_구간의_길이의_합과_동일하다() {
            // given
            final Section up = new Section(잠실, 선릉, 10);
            final Section down = new Section(선릉, 건대입구, 2);

            // when
            final Section addedSection = up.plus(down);
            // then
            assertThat(addedSection.distance()).isEqualTo(12);
            assertThat(addedSection.up()).isEqualTo(잠실);
            assertThat(addedSection.down()).isEqualTo(건대입구);
        }

        @Test
        void 연속되지_않은_두_구간을_더할시_예외() {
            // given
            final Section up = new Section(잠실, 선릉, 10);
            final Section down = new Section(잠실나루, 건대입구, 2);

            // when & then
            final String message = assertThrows(LineException.class, () ->
                    down.plus(up)
            ).getMessage();
            assertThat(message).contains("연속되지 않은 두 구간을 더할 수 없습니다.");
        }
    }

    @Nested
    class 구간_사이의_차이를_구할_떄 {

        @Test
        void 큰_구간에서_작은_구간을_뺀_나머지를_구할_수_있다() {
            // given
            final Section section1 = new Section(잠실, 선릉, 10);
            final Section section2 = new Section(잠실나루, 선릉, 8);

            // when
            final Section remain = section1.minus(section2);

            // then
            assertAll(
                    () -> assertThat(remain.up()).isEqualTo(잠실),
                    () -> assertThat(remain.down()).isEqualTo(잠실나루),
                    () -> assertThat(remain.distance()).isEqualTo(2)
            );
        }

        @Test
        void 빼려는_구간의_크기가_더_크거나_동일하다면_예외() {
            // given
            final Section section1 = new Section(잠실, 선릉, 10);
            final Section section2 = new Section(잠실나루, 선릉, 10);

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    section1.minus(section2)
            ).exceptionType();
            assertThat(exceptionType).isEqualTo(NON_POSITIVE_DISTANCE);
        }

        @Test
        void 겹치는_역이_없어_뺼_수_없다면_예외() {
            // given
            final Section section1 = new Section(잠실, 선릉, 10);
            final Section section2 = new Section(없는역, 없는역2, 1);

            // when & then
            final String message = assertThrows(LineException.class, () ->
                    section1.minus(section2)
            ).getMessage();
            assertThat(message).contains("두 구간은 뺄 수 없는 관계입니다.");
        }
    }
}
