package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.common.exception.SubwayIllegalArgumentException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class LineInfoTest {

    @Test
    void 이름은_50자를_넘길_수_없다() {
        String name = "a".repeat(51);
        String color = "GREEN";

        assertThatThrownBy(() -> new LineInfo(name, color))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("이름은 50자 이하여야합니다.");
    }

    @Test
    void 색상은_20자를_넘길_수_없다() {
        String name = "2호선";
        String color = "a".repeat(21);

        assertThatThrownBy(() -> new LineInfo(name, color))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("색상은 20자 이하여야합니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void 이름은_공백일_수_없다(String name) {
        assertThatThrownBy(() -> new LineInfo(name, "GREEN"))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void 색상은_공백일_수_없다(String color) {
        assertThatThrownBy(() -> new LineInfo("2호선", color))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("색상은 비어있을 수 없습니다.");
    }

    @Test
    void 이름에_공백이_포함될_경우_공백을_제거한다() {
        String name = "   a ";

        LineInfo line = new LineInfo(name, "GREEN");

        assertThat(line.getName()).isEqualTo("a");
    }

    @Test
    void 색상에_공백이_포함될_경우_공백을_제거한다() {
        String color = "   a ";

        LineInfo line = new LineInfo("2호선", color);

        assertThat(line.getColor()).isEqualTo("a");
    }


    @Test
    void 노선_정보를_생성한다() {
        String name = "2호선";
        String color = "GREEN";

        assertDoesNotThrow(() -> new LineInfo(name, color));
    }
}
