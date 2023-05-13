package subway.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class StationDomainTest {

    @Test
    void 역을_생성한다() {
        assertAll(
                () -> assertDoesNotThrow(() -> new StationDomain("루카")),
                () -> assertDoesNotThrow(() -> new StationDomain("헤나"))
        );
    }

    @Test
    void 역_이름이_null이거나_공백일_경우_예외가_발생한다() {
        assertAll(
                () -> assertThatThrownBy(() -> new StationDomain(null))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new StationDomain(""))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new StationDomain(" "))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void 역_이름_길이가_1미만_10초과일_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new StationDomain("01234567891"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
