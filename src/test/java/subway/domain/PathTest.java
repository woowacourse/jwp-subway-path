package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Direction;
import subway.service.domain.Distance;
import subway.service.domain.LineProperty;
import subway.service.domain.Path;
import subway.service.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    @Test
    @DisplayName("Path 를 생성한다.")
    void createPath() {
        Station station = new Station("pathTest");
        LineProperty IGNORED = new LineProperty(1L, "1", "1");

        Path path = new Path(Direction.UP, IGNORED, station, Distance.from(10));

        assertThat(path.getDirection()).isEqualTo(Direction.UP);
        assertThat(path.getNextStation()).isEqualTo(station);
        assertThat(path.getDistance()).isEqualTo(10);
    }

}
