package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class StationLineTest {

    @Mock
    Station station;

    @Mock
    Line line;

    @DisplayName("생성한다.")
    @Test
    void create() {
        assertDoesNotThrow(() -> new StationLine(station, line));
    }
}
