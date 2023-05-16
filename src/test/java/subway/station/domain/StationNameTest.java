package subway.station.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class StationNameTest {

    @ParameterizedTest(name = "이름의 값으로 {0} 가 들어올 경우 오류를 던진다.")
    @NullAndEmptySource
    @CsvSource(value = {"역", "일이삼사오육칠팔구십일", "강g남"})
    void invalidName(final String value) {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> StationName.from(value)).isInstanceOf(IllegalArgumentException.class);
    }
}
