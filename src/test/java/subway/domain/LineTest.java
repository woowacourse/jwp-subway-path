package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;

class LineTest {

    @Test
    @DisplayName("name과 color로 Line을 생성할 수 있다.")
    void createWithTwoParameters() {
        // given
        final String name = "2호선";
        final String color = "bg-green-600";
        // when
        final Line line = new Line(name, color);
        // then
        assertEquals(name, line.getName());
        assertEquals(color, line.getColor());
    }

    @Test
    @DisplayName("id, name, color, sections로 Line을 생성할 수 있다.")
    void createWithFourParameters() {
        // given
        final Long id = 1L;
        final String name = "2호선";
        final String color = "bg-green-600";
        final Section section = new Section(new Station("용산역"), new Station("죽전역"), 10);
        final Sections sections = new Sections(List.of(section));
        // when
        final Line line = new Line(id, name, color, sections);
        // then
        assertEquals(id, line.getId());
        assertEquals(name, line.getName());
        assertEquals(color, line.getColor());
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("name이 공백일 경우, 예외가 발생한다.")
    void validateBlank(final String name) {
        assertThatThrownBy(() -> new Line(name, "bg-green-600"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("name이 5글자보다 클 경우, 예외가 발생한다.")
    void validateLength() {
        // given
        final String name = "123456";
        // when & then
        assertThatThrownBy(() -> new Line(name, "bg-green-600"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("color가 공백일 경우, 예외가 발생한다.")
    void validateColorBlank(final String color) {
        assertThatThrownBy(() -> new Line("2호선", color))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("color가 5글자보다 작을 경우, 예외가 발생한다.")
    void validateColorLengthLowerBound() {
        // given
        final String color = "1234";
        // when & then
        assertThatThrownBy(() -> new Line("2호선", color))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("color가 20글자보다 클 경우, 예외가 발생한다.")
    void validateColorLengthUpperBound() {
        // given
        final String color = "123456789012345678901";
        // when & then
        assertThatThrownBy(() -> new Line("2호선", color))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
