package subway.subwayMap.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.domain.lineDetail.domain.LineDetail;
import subway.domain.station.domain.Station;
import subway.domain.subwayMap.domain.SubwayMap;

import java.util.List;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SubwayMapServiceTest {

    @Autowired
    private SubwayMap subwayMap;

    @Test
    void PostConstruct_테스트() {
        List<Station> stations = subwayMap.getSubwayMapByLine(new LineDetail(1L, "2호선", "초록색"));
        Assertions.assertThat(stations).containsExactly(
                new Station(1L, "신림역"),
                new Station(2L, "봉천역"),
                new Station(3L, "서울대입구역"),
                new Station(4L, "낙성대역"),
                new Station(5L, "사당역"),
                new Station(6L, "방배역"),
                new Station(7L, "서초역")
        );
    }
}
