package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubwaysTest {

    @DisplayName("전체 지하철 노선도를 생성할 수 있다.")
    @Test
    void create() {
        assertDoesNotThrow(() -> Subways.from(List.of(
                SectionFixture.SECTION_START, SectionFixture.SECTION_MIDDLE_1,
                SectionFixture.SECTION_MIDDLE_2, SectionFixture.SECTION_MIDDLE_3,
                SectionFixture.SECTION_END)));
    }
}
