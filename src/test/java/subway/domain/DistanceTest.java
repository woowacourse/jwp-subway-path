package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class DistanceTest {

    @Test
    @DisplayName("생성자를 호출할 때 정상적으로 생성")
    void constructor_success() {
        //when, then
        assertThatCode(() -> new Distance(1)).doesNotThrowAnyException();
    }
}
