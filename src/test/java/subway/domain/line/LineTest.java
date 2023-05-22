package subway.domain.line;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    @Test
    void 노선을_생성한다() {
        String name = "2호선";
        String color = "GREEN";

        assertDoesNotThrow(() -> new Line(name, color, 0));
    }
}
