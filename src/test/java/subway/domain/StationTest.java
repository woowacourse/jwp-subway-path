package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;

class StationTest {

    @Test
    @DisplayName("name으로 Station을 생성할 수 있다.")
    void createWithOneParameter() {
        // given
        final String name = "반월당역";
        // when
        final Station station = new Station(name);
        // then
        assertEquals(name, station.getName());
    }

    @Test
    @DisplayName("id와 name으로 Station을 생성할 수 있다.")
    void createWithTwoParameters() {
        // given
        final Long id = 1L;
        final String name = "반월당역";
        // when
        final Station station = new Station(id, name);
        // then
        assertEquals(id, station.getId());
        assertEquals(name, station.getName());
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("name이 공백일 경우 예외가 발생한다.")
    void validateBlank(final String name) {
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("name이 15글자보다 클 경우 예외가 발생한다.")
    void validateLength() {
        // given
        final String name = "1234567890123456";
        // when & then
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
