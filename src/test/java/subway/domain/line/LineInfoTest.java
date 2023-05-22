package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.fare.Fare;
import subway.domain.line.LineInfo;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class LineInfoTest {

    @Nested
    class 이름은_ {

        @Test
        void _50자를_넘길_수_없다() {
            String name = "a".repeat(51);
            String color = "GREEN";
            int surcharge = 1000;

            assertThatThrownBy(() -> new LineInfo(name, color, surcharge))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("이름은 50자 이하여야합니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "\t", "\n"})
        void _공백일_수_없다(String name) {
            assertThatThrownBy(() -> new LineInfo(name, "GREEN", 1000))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("이름은 비어있을 수 없습니다.");
        }

        @Test
        void 공백이_포함될_경우_공백을_제거한다() {
            String name = "   a ";

            LineInfo line = new LineInfo(name, "GREEN", 1000);

            assertThat(line.getName()).isEqualTo("a");
        }
    }

    @Nested
    class 색상은_ {

        @Test
        void _20자를_넘길_수_없다() {
            String name = "2호선";
            String color = "a".repeat(21);
            int surcharge = 1000;

            assertThatThrownBy(() -> new LineInfo(name, color, surcharge))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("색상은 20자 이하여야합니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "\t", "\n"})
        void 공백일_수_없다(String color) {
            assertThatThrownBy(() -> new LineInfo("2호선", color, 1000))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("색상은 비어있을 수 없습니다.");
        }

        @Test
        void 공백이_포함될_경우_공백을_제거한다() {
            String color = "   a ";

            LineInfo line = new LineInfo("2호선", color, 1000);

            assertThat(line.getColor()).isEqualTo("a");
        }
    }

    @Nested
    class 추가_요금은_ {

        @Test
        void null이_들어오면_기본_요금으로_책정된다() {
            // when
            LineInfo lineInfo = new LineInfo("2호선", "GREEN", null);

            // then
            assertThat(lineInfo.getSurcharge()).isEqualTo(new Fare(0));
        }

        @Test
        void 기본요금보다_적은_금액이_입력되면_예외() {
            // when then
            assertThatThrownBy(() -> new LineInfo("2호선", "GREEN", -1))
                    .isInstanceOf(SubwayIllegalArgumentException.class)
                    .hasMessage("노선 추가 요금은 0원 이상이어야합니다.");
        }
    }

    @Test
    void 노선_정보를_생성한다() {
        String name = "2호선";
        String color = "GREEN";
        int surcharge = 0;

        assertDoesNotThrow(() -> new LineInfo(name, color, surcharge));
    }
}
