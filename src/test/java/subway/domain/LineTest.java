package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidException;

class LineTest {
    @Test
    @DisplayName("of() : 입력되는 구간이 없는 경우, 생성되는 Line은 빈 Sections를 갖는다.")
    void ofWhenEmptySections() {
        // given
        Line line = Line.of("2호선", List.of());

        // then
        assertAll(
                () -> assertThat(line.getSectionsByList()).isEmpty(),
                () -> assertThat(line.getStations()).isEmpty()
        );
    }

    @Test
    @DisplayName("deleteStation() : 노선이 비어 있을 때 역을 지우면 예외를 던진다.")
    void deleteStationWhenEmptySectionThrowsInvalidException() {
        // given
        Line line = Line.of("2호선", List.of());
        Station stationForDelete = new Station("존재하지않는역");

        // then
        assertThatThrownBy(() -> line.deleteStation(stationForDelete))
                .isInstanceOf(InvalidException.class)
                .hasMessage("노선에 구간이 존재하지 않아 삭제할 수 없습니다.");
    }
}
