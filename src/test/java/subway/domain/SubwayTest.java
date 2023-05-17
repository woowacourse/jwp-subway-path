package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.StreamsBuilderFactoryBeanCustomizer;
import subway.service.domain.Distance;
import subway.service.domain.Line;
import subway.service.domain.LineProperty;
import subway.service.domain.Section;
import subway.service.domain.Sections;
import subway.service.domain.SingleLine;
import subway.service.domain.Station;
import subway.service.domain.Subway;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayTest {

    static final List<Line> lines = List.of(
            createLine(1L, "7호선", "rg-olive-600", "가산", "남구로", "대림", "신풍"),
            createLine(2L, "1호선", "rg-blue-600", "구로", "남구로", "독산"),
            createLine(3L, "2호선", "rg-green-600", "신도림", "대림", "구로디지털단지")
    );

    @Test
    @DisplayName("Subway 가 가지고 있는 Line 에서 ID 를 이용해 해당 Line 의 노선을 가져온다.")
    void getSingleLine() {
        Subway subway = new Subway(lines);

        SingleLine singleLine = subway.getSingleLine(1L);

        assertThat(singleLine.getLineProperty().getId()).isEqualTo(1);
        assertThat(singleLine.getLineProperty().getName()).isEqualTo("7호선");
        assertThat(singleLine.getLineProperty().getColor()).isEqualTo("rg-olive-600");
        assertThat(singleLine.getStations())
                .containsAll(List.of(
                        new Station("가산"),
                        new Station("남구로"),
                        new Station("대림"),
                        new Station("신풍")));
    }

    @Test
    @DisplayName("Subway 가 가지고 있지 않은 Line 을 찾으려하면 예외가 발생한다.")
    void getSingleLine_fail() {
        Subway subway = new Subway(lines);

        assertThatThrownBy(() -> subway.getSingleLine(4L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Line createLine(Long lineId,
                           String lineName,
                           String lineColor,
                           String... stationsName) {
        LineProperty lineProperty = new LineProperty(lineId, lineName, lineColor);
        Distance ignored = Distance.from(10);
        List<Section> sections = new ArrayList<>();

        for (int i = 0; i < stationsName.length - 1; i++) {
            sections.add(new Section(
                    new Station(stationsName[i]),
                    new Station(stationsName[i + 1]), ignored));
        }

        return new Line(lineProperty, new Sections(sections));
    }

}
