package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class SectionInfoTest {

    SectionInfo sectionInfo;

    @BeforeEach
    void setUp() {
        sectionInfo = SectionInfo.of(Distance.from(5), Direction.DOWN);
    }

    @Test
    void calculateMiddleDistance_메소드는_distance를_전달하면_그_차이를_반환한다() {
        final Distance actual = sectionInfo.calculateMiddleDistance(Distance.from(3));

        assertThat(actual.getDistance()).isEqualTo(2);
    }

    @ParameterizedTest(name = "길이 {0}를 전달하면 예외가 발생한다.")
    @ValueSource(ints = {5, 6})
    void calculateMiddleDistance_메소드는_전달한_distance가_같거나_크다면_예외가_발생한다(final int invalidDistance) {
        assertThatThrownBy(() -> sectionInfo.calculateMiddleDistance(Distance.from(invalidDistance)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되는 구간 중간에 다른 역이 존재합니다.");
    }

    @ParameterizedTest(name = "방향 {0}을 전달하면 {1}을 반환한다.")
    @CsvSource(value = {"UP:false", "DOWN:true"}, delimiter = ':')
    @DisplayName("만약 방향을 전달하면 구간의 방향이 일치하는지 여부를 반환한다.")
    void matchesByDirection_메소드는_방향을_전달하면_구간의_방향이_일치하는지_여부를_반환한다(
            final Direction direction,
            final boolean expected
    ) {
        final boolean actual = sectionInfo.matchesByDirection(direction);

        assertThat(actual).isSameAs(expected);
    }
}
