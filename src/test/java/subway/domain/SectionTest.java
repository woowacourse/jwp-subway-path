package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class SectionTest {

    public static final Distance DISTANCE = new Distance(10);
    @Mock
    Station station;
    @Mock
    Line line;

    @DisplayName("생성한다")
    @Test
    void create() {
        assertDoesNotThrow(() -> new Section(station, station, line, DISTANCE, false));
    }

    @DisplayName("station이 null이면 예외를 발생한다.")
    @Test
    void throwExceptionWhenStationIsNull() {

        assertThatThrownBy(() -> new Section(null, station, line, DISTANCE, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("line이 null이면 예외를 발생한다.")
    @Test
    void throwExceptionWhenLineIsNull() {

        assertThatThrownBy(() -> new Section(station, station, null, DISTANCE, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("distance가 null이면 예외를 발생한다.")
    @Test
    void throwExceptionWhenDistanceIsNull() {

        assertThatThrownBy(() -> new Section(station, station, line, null, false))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
