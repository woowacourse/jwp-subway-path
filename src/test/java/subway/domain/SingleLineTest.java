package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.LineProperty;
import subway.service.domain.SingleLine;
import subway.service.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SingleLineTest {

    @Test
    @DisplayName("SingleLine을 생성한다.")
    void createSingleLine() {
        LineProperty lineProperty = new LineProperty("7호선", "rg-olive-600");
        Station first = new Station("first");
        Station second = new Station("seconde");
        List<Station> stations = List.of(first, second);

        SingleLine singleLine = SingleLine.of(lineProperty, stations);

        assertThat(singleLine.getLineProperty().getName()).isEqualTo("7호선");
        assertThat(singleLine.getLineProperty().getColor()).isEqualTo("rg-olive-600");
        assertThat(singleLine.getStations()).allMatch(station -> station.equals(first) || station.equals(second));
    }

}
