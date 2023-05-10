package subway.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    @DisplayName("역 두 개와 거리로 생성한다.")
    void create() {
        //given
        Station station1 = new Station("잠실");
        Station station2 = new Station("잠실나루");
        int distance = 5;
        String name = "2호선";
        String color = "초록색";

        //when
        final var line = Line.of(name, color, new StationEdge(station1, station2, distance));

        //then
        Assertions.assertThat(line)
                .isInstanceOf(Line.class)
                .isNotNull();
    }

}