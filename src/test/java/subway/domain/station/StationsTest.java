package subway.domain.station;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.exception.ErrorCode.STATION_NAME_DUPLICATED;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.GlobalException;

class StationsTest {

    @Test
    @DisplayName("역 이름이 중복되면 예외가 발생한다.")
    void validateDuplication_fail_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Stations stations = new Stations(Arrays.asList(잠실역, 선릉역));

        // expect
        final Station 중복된_잠실역 = new Station("잠실역");
        assertThatThrownBy(() -> stations.validateDuplication(중복된_잠실역))
            .isInstanceOf(GlobalException.class)
            .extracting("errorCode")
            .isEqualTo(STATION_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("중복되지 않은 역이 존재하는지 검증한다.")
    void validateDuplication_success_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Stations stations = new Stations(new ArrayList<>());

        // expected
        assertDoesNotThrow(() -> stations.validateDuplication(잠실역));
    }
}
