package subway.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    @DisplayName("구간을 등록한다.")
    public void createSection_success() {
        Station upStation = new Station("잠실역");
        Station downStation = new Station("잠실나루역");

        assertDoesNotThrow(
                () -> new Section(upStation, downStation, 10)
        );
    }
}
