package subway.subwayMap.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import subway.domain.lineDetail.domain.LineDetail;
import subway.domain.station.domain.Station;
import subway.domain.subwayMap.domain.SubwayMap;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SubwayMapTest {

    private SubwayMap subwayMap;
    private LineDetail lineDetail;

    @BeforeEach
    void init() {
        lineDetail = new LineDetail("2호선", "초록색");
        List<Station> stations = List.of(new Station("a"), new Station("b"), new Station("c"));
        subwayMap = new SubwayMap();
        subwayMap.put(lineDetail, stations);
    }

    @Test
    void SubwayMap_에서_좌측_끝에_역_추가_테스트() {
        subwayMap.addStation(lineDetail, new Station("a"), new Station("d"), false);
        List<Station> subwayMapByLine = subwayMap.getSubwayMapByLine(lineDetail);

        Assertions.assertThat(subwayMapByLine).containsExactly(
                new Station("d"),
                new Station("a"),
                new Station("b"),
                new Station("c")
        );
    }

    @Test
    void SubwayMap_에서_우측_끝에_역_추가_테스트() {
        subwayMap.addStation(lineDetail, new Station("c"), new Station("d"), true);
        List<Station> subwayMapByLine = subwayMap.getSubwayMapByLine(lineDetail);

        Assertions.assertThat(subwayMapByLine).containsExactly(
                new Station("a"),
                new Station("b"),
                new Station("c"),
                new Station("d")
        );
    }

    @Test
    void SubwayMap_에서_역사이에_역_추가_테스트() {
        subwayMap.addStation(lineDetail, new Station("a"), new Station("d"), true);
        List<Station> subwayMapByLine = subwayMap.getSubwayMapByLine(lineDetail);

        Assertions.assertThat(subwayMapByLine).containsExactly(
                new Station("a"),
                new Station("d"),
                new Station("b"),
                new Station("c")
        );
    }

    @Test
    void SubwayMap_에서_역_삭제_테스트() {
        subwayMap.deleteStation(lineDetail, new Station("b"));
        Assertions.assertThat(subwayMap.getSubwayMapByLine(lineDetail))
                .containsExactly(new Station("a"), new Station("b"));
    }
}
