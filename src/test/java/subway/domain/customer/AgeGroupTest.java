package subway.domain.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeGroupTest {

    @ParameterizedTest
    @CsvSource(value = {"5:PREFERENTIAL", "6:CHILD", "12:CHILD", "13:TEENAGER", "18:TEENAGER", "19:ADULT", "64:ADULT", "65:PREFERENTIAL"}, delimiter = ':')
    @DisplayName("연령에 맞는 AgeGroup을 반환한다.")
    void ageGroupTest(int age, AgeGroup expectAgeGroup) {
        // when
        AgeGroup ageGroup = AgeGroup.from(age);

        // then
        assertThat(ageGroup).isEqualTo(expectAgeGroup);
    }
}