package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgePolicyTest {

    @ParameterizedTest(name = "나이가 {0}이면 {1}이다.")
    @CsvSource(delimiter = ':', value = {"19:ADULT", "13:TEENAGER", "6:CHILD", "4:BABY"})
    void 나이별_정책_확인(int age, AgePolicy agePolicy) {
        Assertions.assertThat(AgePolicy.of(age))
                .isEqualTo(agePolicy);
    }
}
