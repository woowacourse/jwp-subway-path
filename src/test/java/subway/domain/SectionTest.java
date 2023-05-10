package subway.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Section은 ")
class SectionTest {

    @Test
    @DisplayName("id, 상행 방향 역(Station) 정보, 하행 방향 역(Station) 정보, 역 사이 거리를 갖는다.")
    void sectionCreateTest() {
        // given
        Station upwardStation = Station.of(2L, "잠실나루");
        Station downwardStation = Station.of(1L, "잠실");

        // then
        assertDoesNotThrow(() -> Section.of(upwardStation, downwardStation, 10));
    }
}
