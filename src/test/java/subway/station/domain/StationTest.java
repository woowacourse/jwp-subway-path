package subway.station.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SuppressWarnings("NonAsciiCharacters")
class StationTest {
    @Test
    void 역_이름_정상_입력() {
        // expect
        assertThatNoException()
                .isThrownBy(() -> new Station("강남역"));
    }
    
    @ParameterizedTest(name = "{displayName} : name = {0}")
    @NullAndEmptySource
    void 역_이름이_비어있으면_예외(final String name) {
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Station(name));
    }
}
