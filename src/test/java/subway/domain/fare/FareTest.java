package subway.domain.fare;

import org.junit.jupiter.api.Test;
import subway.domain.line.Line;

public class FareTest {

    @Test
    void name() {
        Line line = Line.of("1호선", "ㅁㄴㅇㄹ", 1000);
        // 최단 경로를 알 수 있다.
        // 최단 경로를 구성하는 구간을 받을 수 있음
        // Path 도메인 객체가 List<Section>을 가짐
        // 구간들에는 line이 있어
        // 구간들이 가진 line들을 List<Line>을 만들어서 farePolicy에 넣어줘
        // farePolicy는 여러 line들 중에 가장 비싼 extraFare를 계산해서 반환.
    }
}
