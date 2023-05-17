package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Section은 ")
class SectionTest {

    @Test
    @DisplayName("id, 상행 방향 역(Station) 정보, 하행 방향 역(Station) 정보, 역 사이 거리를 갖는다.")
    void sectionCreateTest() {
        // given
        Station upwardStation = Station.of(2L, "잠실나루");
        Station downwardStation = Station.of(1L, "잠실");
        Line line = Line.of("2호선", "초록색", 0);

        // then
        assertDoesNotThrow(() -> Section.of(upwardStation, downwardStation, 10));
    }

    @Test
    @DisplayName("상행 방향 역과 하행 방향 역이 동일하면 예외처리한다.")
    void sectionHasSameStationExceptionTest() {
        // given
        Station upwardStation = Station.of(1L, "잠실나루");
        Line line = Line.of("2호선", "초록색", 0);

        // then
        assertThatThrownBy(() -> Section.of(upwardStation, upwardStation, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 구간을 구성하는 역은 동일한 역일 수 없습니다.");
    }
}
