package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.exception.ApiIllegalStateException;

class AgePolicyTest {

    @ParameterizedTest(name = "나이가 {0}이면 {1}이다.")
    @CsvSource(delimiter = ':', value = {"19:ADULT", "13:TEENAGER", "6:CHILD", "4:BABY"})
    void 나이별_정책_확인(int age, AgePolicy agePolicy) {
        Assertions.assertThat(AgePolicy.from(age))
                .isEqualTo(agePolicy);
    }

    @Test
    void 유효하지_않은_나이가_들어오면_예외() {
        Assertions.assertThatThrownBy(() -> AgePolicy.from(-1))
                .isInstanceOf(ApiIllegalStateException.class)
                .hasMessage("해당 나이는 유효하지 않습니다.");
    }
}
