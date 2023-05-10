package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {


    @DisplayName("라인을 최초 생성한다.")
    @Test
    void createLine() {
        // given
        Line line = Line.createLine("2호선", new Station("잠실"), new Station("잠실나루"), 10);

        // when
        line.addEdge(new Station("강변"), new Station("잠실"), 3);

        // then
        assertThat(line.getEdges()).hasSize(2);
    }

    // TODO : 라인 생성 검증 테스트
}
