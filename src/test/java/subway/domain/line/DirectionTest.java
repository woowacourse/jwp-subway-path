package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @DisplayName("생성 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"left", "right"})
    void generate(String value) {
        //when
        Direction direction = Direction.of(value);

        //then
        assertEquals(value.toUpperCase(), direction.getDirection().toUpperCase());
    }
}
