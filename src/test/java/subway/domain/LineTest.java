package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {
    @Test
    void 입력되는_구간이_없는_경우_생성되는_Line은_빈_Sections를_갖는다() {
        // given
        Line 노선 = Line.of(1L, "2호선", List.of());

        // then
        assertAll(
                () -> assertThat(노선.getSectionsByList()).isEmpty(),
                () -> assertThat(노선.getStations()).isEmpty()
        );
    }

    @Test
    void 노선이_비어있을_때_역을_지우면_예외를_던진다() {
        // given
        Line 노선 = Line.of(1L, "2호선", List.of());
        Station 삭제할_역 = new Station(1L, "존재하지않는역");

        // then
        assertThatThrownBy(() -> 노선.deleteStation(삭제할_역))
                .isInstanceOf(InvalidException.class)
                .hasMessage("노선에 구간이 존재하지 않아 삭제할 수 없습니다.");
    }
}
