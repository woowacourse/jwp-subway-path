package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LineTest {

    @ParameterizedTest
    @MethodSource("createLine")
    @DisplayName("line의 section이 비어 있으면 false, 그렇지 않으면 true를 반환한다.")
    void check_section_exist(Line line, boolean expect) {
        // when
        boolean result = line.isEmpty();

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> createLine() {
        return Stream.of(
           Arguments.arguments(new Line(1L, "2호선", "#123456", List.of()), false),
           Arguments.arguments(new Line(1L, "2호선", "#123456", List.of(new Section(new Station("잠실"), new Station("선릉"), 10))), true)
        );
    }

}