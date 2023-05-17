package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayTest {

    static final Map<Long, List<Station>> stationsMap = Map.of(
            1L, createStations("가산", "남구로", "대림", "신풍"),
            2L, createStations("구로", "남구로", "독산"),
            3L, createStations("신도림", "대림", "구로디지털단지")
    );

    static final List<Line> lines = List.of(
            createLine(1L, "7호선", "rg-olive-600", stationsMap.get(1L)),
            createLine(2L, "1호선", "rg-blue-600", stationsMap.get(2L)),
            createLine(3L, "2호선", "rg-green-600", stationsMap.get(3L))
    );

    @Test
    @DisplayName("Subway 가 가지고 있는 Line 에서 ID 를 이용해 해당 Line 의 노선을 가져온다.")
    void getSingleLine() {
        Subway subway = new Subway(lines);

        SingleLine singleLine = subway.getSingleLine(1L);

        assertThat(singleLine.getLineProperty().getId()).isEqualTo(1);
        assertThat(singleLine.getLineProperty().getName()).isEqualTo("7호선");
        assertThat(singleLine.getLineProperty().getColor()).isEqualTo("rg-olive-600");
        assertThat(singleLine.getStations()).containsAll(createStations("가산", "남구로", "대림", "신풍"));
    }

    @Test
    @DisplayName("Subway 가 가지고 있는 모든 Line 의 노선을 조회한다.")
    void getAllLine_notEmpty() {
        Subway subway = new Subway(lines);

        List<SingleLine> singleLines = subway.getAllLine();

        assertThat(singleLines)
                .hasSize(3)
                .allMatch(singleLine ->
                        isSameStationsOnLine(
                                singleLine.getStations(),
                                stationsMap.get(singleLine.getLineProperty().getId())
                        )
                );
    }

    @Test
    @DisplayName("Subway 가 아무 라인을 가지고 있지 않아, 빈 리스트를 반환한다.")
    void getAllLine_empty() {
        Subway subway = new Subway(new ArrayList<>());

        List<SingleLine> allLine = subway.getAllLine();

        assertThat(allLine).isEmpty();
    }

    @Test
    @DisplayName("Subway 가 가지고 있지 않은 Line 을 찾으려하면 예외가 발생한다.")
    void getSingleLine_fail() {
        Subway subway = new Subway(lines);

        assertThatThrownBy(() -> subway.getSingleLine(4L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static boolean isSameStationsOnLine(List<Station> singleLineStations, List<Station> standardStation) {
        if (singleLineStations.size() != standardStation.size()) {
            return false;
        }

        for (int i = 0; i < singleLineStations.size(); i++) {
            if (!singleLineStations.get(i).equals(standardStation.get(i))) {
                return false;
            }
        }

        return true;
    }

    static List<Station> createStations(String... stationsName) {
        List<Station> stations = new ArrayList<>();

        for (String stationName : stationsName) {
            stations.add(new Station(stationName));
        }

        return stations;
    }

    static Line createLine(Long lineId,
                           String lineName,
                           String lineColor,
                           List<Station> stations) {
        LineProperty lineProperty = new LineProperty(lineId, lineName, lineColor);
        Distance ignored = Distance.from(10);
        List<Section> sections = new ArrayList<>();

        for (int i = 0; i < stations.size() - 1; i++) {
            sections.add(new Section(
                    stations.get(i),
                    stations.get(i + 1),
                    ignored));
        }

        return new Line(lineProperty, new Sections(sections));
    }

}
