package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@ExtendWith(MockitoExtension.class)
class StationTest {

    @Mock
    Section mockSection;

    @DisplayName("생성한다.")
    @Test
    void create() {
        assertDoesNotThrow(() -> new Station(1L, "luca", List.of(mockSection)));
    }

    @DisplayName("이름이 공백이거나 null일 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throwExceptionWhenNameIsNullOrEmpty(final String name) {
        assertThatThrownBy(() -> new Station(1L, name, List.of(mockSection)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
