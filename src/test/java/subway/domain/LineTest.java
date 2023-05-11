package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @DisplayName("비어 있는 노선에 역을 추가한다.")
    @Test
    void initStations_success() {
        //given
        Line line = new Line("2호선", "RED");

        //then
        assertAll(
            () -> assertDoesNotThrow(() -> line.initStations(new Station(1L, "강남"), new Station(2L, "방배"))),
            () -> assertThat(line.getStations().isEmpty()).isFalse()
        );
    }

    @DisplayName("비어 있는 노선에 역을 하나 추가하면 예외가 발생한다.")
    @Test
    void addBeforeAt_fail() {
        //given
        Line line = new Line("2호선", "RED");

        //then
        Assertions.assertThatThrownBy(() -> line.addBeforeAt(new Station(1L, "강남"), new Station(2L, "방배")))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("비어 있는 노선에 역을 하나 추가하면 예외가 발생한다.")
    @Test
    void addLast_fail() {
        //given
        Line line = new Line("2호선", "RED");

        //then
        Assertions.assertThatThrownBy(() -> line.addLast(new Station("강남")))
            .isInstanceOf(IllegalStateException.class);
    }
}