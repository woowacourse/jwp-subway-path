package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MyLineTest {


    @DisplayName("라인을 최초 생성한다.")
    @Test
    void createLine() {
        // given
        MyLine line = MyLine.createLine("2호선", new MyStation("잠실"), new MyStation("잠실나루"), 10);

        // when
        line.addEdge(new MyStation("강변"), new MyStation("잠실"), 3);

        // then
        assertThat(line.getEdges()).hasSize(2);
    }

    // TODO : 라인 생성 검증 테스트
}
