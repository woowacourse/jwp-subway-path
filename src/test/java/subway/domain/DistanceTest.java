package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("value가 음수라면 예외발생")
    void test_negative_value_exception(int value){
        //when, then
        assertThatThrownBy(()-> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("거리는 양의 정수여야 합니다");
    }

    @Test
    @DisplayName("value가 양의 정수라면 정상적으로 생성")
    void test_negative_value_exception(){
        //when, then
        assertThatCode(()-> new Distance(1)).doesNotThrowAnyException();
    }

}
