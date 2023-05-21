package subway.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 경로 테스트")
class SubwayRouteEdgeTest {
    @Test
    void 구간과_라인을_입력받아_경로를_생성한다() {
        // given
        Section 첫번째_구간 = Section.of(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10);
        Section 두번째_구간 = Section.of(new Station(2L, "잠실새내역"), new Station(3L, "종합운동장역"), 15);
        Line 노선 = Line.of(1L, "2호선", List.of(첫번째_구간, 두번째_구간));

        // then
        assertDoesNotThrow(() -> SubwayRouteEdge.of(노선, 첫번째_구간));
    }
}
