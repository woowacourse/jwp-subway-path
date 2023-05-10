package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.domain.fixture.SectionFixtures.createSection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Section 은(는)")
class SectionTest {

    @Test
    void 구간_거리가_양수가_아니면_예외() {
        // given
        final Station 출발역 = new Station("출발역");
        final Station 종착역 = new Station("종착역");

        // when & then
        assertThatThrownBy(() ->
                new Section(출발역, 종착역, 0)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class 구간_사이의_차이를_구할_떄 {

        @Test
        void 큰_구간에서_작은_구간을_뺀_나머지를_구할_수_있다() {
            // given
            final Section section1 = createSection("잠실역", "마산역", 10);
            final Section section2 = createSection("잠실나루역", "마산역", 8);

            // when
            final Section remain = section1.minus(section2);

            // then
            assertAll(
                    () -> assertThat(remain.up().name()).isEqualTo("잠실역"),
                    () -> assertThat(remain.down().name()).isEqualTo("잠실나루역"),
                    () -> assertThat(remain.distance()).isEqualTo(2)
            );
        }

        @Test
        void 빼려는_구간의_크기가_더_크거나_동일하다면_예외() {
            // given
            final Section section1 = createSection("잠실역", "마산역", 10);
            final Section section2 = createSection("잠실나루역", "마산역", 10);

            // when & then
            final String message = assertThrows(IllegalArgumentException.class, () ->
                    section1.minus(section2)
            ).getMessage();
            assertThat(message).isEqualTo("현재 구간이 더 작아 차이를 구할 수 없습니다.");
        }

        @Test
        void 겹치는_역이_없어_뺼_수_없다면_예외() {
            // given
            final Section section1 = createSection("잠실역", "마산역", 10);
            final Section section2 = createSection("없는역", "없는역2", 1);

            // when & then
            final String message = assertThrows(IllegalArgumentException.class, () ->
                    section1.minus(section2)
            ).getMessage();
            assertThat(message).isEqualTo("두 구간이 연관관계가 없어 뺄 수 없습니다.");
        }
    }


    @Nested
    class 두_구간의_합을_구할_때 {

        @Test
        void 합쳐진_구간의_길이는_두_구간의_길이의_합과_동일하다() {
            // given
            final Section up = createSection("잠실역", "마산역", 10);
            final Section down = createSection("마산역", "슈퍼말랑역", 2);

            // when
            final Section addedSection = up.plus(down);
            // then
            assertThat(addedSection.distance()).isEqualTo(12);
            assertThat(addedSection.up().name()).isEqualTo("잠실역");
            assertThat(addedSection.down().name()).isEqualTo("슈퍼말랑역");
        }

        @Test
        void 연속되지_않은_두_구간을_더할시_예외() {
            // given
            final Section up = createSection("잠실역", "마산역", 10);
            final Section down = createSection("마산역", "슈퍼말랑역", 2);

            // when & then
            final String message = assertThrows(IllegalArgumentException.class, () ->
                    down.plus(up)
            ).getMessage();
            assertThat(message).isEqualTo("연속되지 않은 두 구간을 더할 수 없습니다.");
        }
    }

    @Test
    void 상대적으로_상행_구간인지_알_수_있다() {
        // given
        final Section up = createSection("잠실역", "마산역", 10);
        final Section down = createSection("마산역", "슈퍼말랑역", 2);

        // when & then
        assertThat(up.isUpThan(down)).isTrue();
        assertThat(down.isUpThan(up)).isFalse();
    }

    @Test
    void 상대적으로_하행_구간인지_알_수_있다() {
        // given
        final Section up = createSection("잠실역", "마산역", 10);
        final Section down = createSection("마산역", "슈퍼말랑역", 2);

        // when & then
        assertThat(up.isDownThan(down)).isFalse();
        assertThat(down.isDownThan(up)).isTrue();
    }

    @Test
    void 상행역_혹은_하행역이_동일한지_판단한다() {
        // given
        final Section section1 = createSection("잠실역", "마산역", 10);
        final Section section2 = createSection("잠실역", "김포공항역", 18);
        final Section section3 = createSection("김치역", "마산역", 12);

        // when & then
        assertAll(
                () -> assertThat(section1.hasSameUpOrDownStation(section2)).isTrue(),
                () -> assertThat(section1.hasSameUpOrDownStation(section3)).isTrue(),
                () -> assertThat(section3.hasSameUpOrDownStation(section2)).isFalse()
        );
    }
}
