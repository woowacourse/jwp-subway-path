package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LineTest {

    private Line line;


    @ParameterizedTest
    @ValueSource(strings = {"호선", "신분당선", "가가가가가가가가가선"})
    @DisplayName("노선 이름이 1자 이상 10자 이하면 정상적으로 역이 생성된다")
    public void createLineTest(String name) {
        assertDoesNotThrow(() -> new Line(null, name, "보라색", 0, new Sections()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "가가가가가가가가가가가가선"})
    @DisplayName("노선 이름이 1자 이하 10자 이상면 예외가 발생한다")
    public void createLineErrorTest(String name) {
        assertThatThrownBy(() -> new Line(null, name, "보라색", 0, new Sections()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("노선 이름은 1자 이상 10자 이하여야 합니다.");
    }

    @Test
    @DisplayName("빈 노선이면 초기 역 2개를 저장한다")
    void addInitialStations() {
        line = new Line(null, "2호선", "초록색", 0, new Sections());

        line.addInitialStations(new Station("강남역"),
                new Station("선릉역"),
                new Distance(10));
        assertThat(line.findStations())
                .contains(new Station("강남역"), new Station("선릉역"));
    }

    @Test
    @DisplayName("빈 노선이 아니면면 초기 역 2개를 저장할 때, 예외가 발생한다")
    void addInitialStationsNotEmpty() {
        line = new Line(null, "2호선", "초록색", 0, new Sections(new ArrayList<>()));
        line.addInitialStations(new Station("강남역"), new Station("선릉역"), new Distance(10));

        assertThatThrownBy(() -> line.addInitialStations(new Station("잠실역"), new Station("잠실새내역"), new Distance(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 노선이 아닙니다.");
    }

    @Test
    @DisplayName("역을 추가로 저장한다")
    void addStation() {
        line = new Line(null, "2호선", "초록색", 0, new Sections(new ArrayList<>()));
        line.addInitialStations(new Station("강남역"), new Station("선릉역"), new Distance(10));

        assertDoesNotThrow(() ->
                line.addStation(new Station("선릉역"), new Station("역삼역"), Direction.DOWN, new Distance(5)));
        assertThat(line.findStations())
                .containsExactlyElementsOf(List.of(new Station("강남역"), new Station("역삼역"), new Station("선릉역")));

    }


}
