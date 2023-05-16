package subway.subwayMap.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import subway.domain.line.domain.Line;
import subway.domain.station.domain.Station;
import subway.domain.subwayMap.domain.SubwayMap;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SubwayMapTest {

    private SubwayMap subwayMap;
    private Line line;

    @BeforeEach
    void init() {
        line = new Line("2호선", "초록색");
        List<Station> stations = List.of(new Station("a"), new Station("b"), new Station("c"));
        subwayMap = new SubwayMap();
        subwayMap.put(line, stations);
    }

    @Test
    void SubwayMap_에서_좌측_끝에_역_추가_테스트() {
        subwayMap.addStation(line, new Station("a"), new Station("d"), false);
        List<Station> subwayMapByLine = subwayMap.getSubwayMapByLine(line);

        Assertions.assertThat(subwayMapByLine).containsExactly(
                new Station("d"),
                new Station("a"),
                new Station("b"),
                new Station("c")
        );
    }

    @Test
    void SubwayMap_에서_우측_끝에_역_추가_테스트() {
        subwayMap.addStation(line, new Station("c"), new Station("d"), true);
        List<Station> subwayMapByLine = subwayMap.getSubwayMapByLine(line);

        Assertions.assertThat(subwayMapByLine).containsExactly(
                new Station("a"),
                new Station("b"),
                new Station("c"),
                new Station("d")
        );
    }

    @Test
    void SubwayMap_에서_역사이에_역_추가_테스트() {
        subwayMap.addStation(line, new Station("a"), new Station("d"), true);
        List<Station> subwayMapByLine = subwayMap.getSubwayMapByLine(line);

        Assertions.assertThat(subwayMapByLine).containsExactly(
                new Station("a"),
                new Station("d"),
                new Station("b"),
                new Station("c")
        );
    }

    @Test
    void SubwayMap_에서_역_삭제_테스트() {
        subwayMap.deleteStation(line, new Station("b"));
        Assertions.assertThat(subwayMap.getSubwayMapByLine(line))
                .containsExactly(new Station("a"), new Station("b"));
    }
}
