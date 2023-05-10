package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidDistanceException;
import subway.exception.NameLengthException;
import subway.utils.StationFixture;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static subway.utils.StationFixture.JAMSIL_NARU_STATION;
import static subway.utils.StationFixture.JAMSIL_STATION;

class SectionTest {

    @Test
    void Section은_하행역_상행역에_해당하는_Station을_갖는다() {
        assertThatNoException()
                .isThrownBy(
                        () -> new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, 5)
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구간의_거리가_양의정수가_아니면_예외를_던진다(int invalidDistance) {
        assertThatThrownBy(() -> new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, invalidDistance))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @Test
    void 구간의_거리가_양의정수가_아니면_예외를_던진다() {
        assertThatNoException().isThrownBy(() -> new Section(JAMSIL_STATION, JAMSIL_NARU_STATION, 1));
    }
}