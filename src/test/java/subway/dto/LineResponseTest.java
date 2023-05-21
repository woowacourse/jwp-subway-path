package subway.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Line;
import subway.domain.core.Section;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineResponseTest {

    @Test
    void 라인을_받아_생성한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));

        // when
        final LineResponse result = LineResponse.from(line);

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("RED"),
                () -> assertThat(result.getSurcharge()).isEqualTo(0),
                () -> assertThat(result.getStations()).containsExactly("A", "B", "C", "D", "E")
        );
    }
}
