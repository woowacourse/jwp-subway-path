package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.section.Section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.조앤;
import static subway.common.fixture.DomainFixture.후추;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 두_역이_같으면_예외를_발생한다() {
        assertThatThrownBy(() -> new Section(후추, 후추, 7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 역을 추가할 수 없습니다.");
    }

    @Test
    void 역을_포함하는지_확인한다() {
        // given
        final Section section = new Section(후추, 디노, 7);

        // expect
        assertSoftly(softly -> {
            softly.assertThat(section.contains(후추)).isTrue();
            softly.assertThat(section.contains(디노)).isTrue();
            softly.assertThat(section.contains(조앤)).isFalse();
        });
    }

    @Test
    void 역을_왼쪽에_포함하는지_확인한다() {
        // given
        final Section section = new Section(후추, 디노, 7);

        // expect
        assertSoftly(softly -> {
            softly.assertThat(section.containsOnLeft(후추)).isTrue();
            softly.assertThat(section.containsOnLeft(디노)).isFalse();
        });
    }

    @Test
    void 역을_오른쪽에_포함하는지_확인한다() {
        // given
        final Section section = new Section(후추, 디노, 7);

        // expect
        assertSoftly(softly -> {
            softly.assertThat(section.containsOnRight(후추)).isFalse();
            softly.assertThat(section.containsOnRight(디노)).isTrue();
        });
    }

    @ParameterizedTest
    @CsvSource({"6, true", "7, false", "8, false"})
    void 역을_추가할_수_있는_거리인지_확인한다(final int distance, final boolean expected) {
        // given
        final Section section = new Section(후추, 디노, 7);

        // when
        final boolean actual = section.isInsertable(distance);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 왼쪽_역을_교체한다() {
        //given
        final Section section = new Section(후추, 디노, 7);

        //when
        final Section insertedSection = section.changeLeft(조앤, 4);

        //then
        assertThat(insertedSection).isEqualTo(new Section(조앤, 디노, 3));
    }

    @Test
    void 오른쪽_역을_교체한다() {
        //given
        final Section section = new Section(후추, 디노, 7);

        //when
        final Section insertedSection = section.changeRight(조앤, 4);

        //then
        assertThat(insertedSection).isEqualTo(new Section(후추, 조앤, 3));
    }
}
