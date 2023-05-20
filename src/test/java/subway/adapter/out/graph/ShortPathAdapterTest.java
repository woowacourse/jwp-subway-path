package subway.adapter.out.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.adapter.out.graph.dto.RouteDto;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ShortPathAdapterTest {

    private ShortPathAdapter shortPathAdapter;

    @BeforeEach
    void setUp() {
        shortPathAdapter = new ShortPathAdapter();
    }

    @Test
    @DisplayName("정상적인 경로가 들어왔을 때 최단경로,요금 반환 테스트")
    void findSortPath() {
        List<Section> sectionList = Arrays.asList(
                new Section(1L, new Station("가"), new Station("나"), 5L),
                new Section(1L, new Station("나"), new Station("다"), 5L),
                new Section(1L, new Station("다"), new Station("라"), 5L),
                new Section(1L, new Station("라"), new Station("마"), 5L),
                new Section(1L, new Station("마"), new Station("바"), 5L),
                new Section(2L, new Station("다"), new Station("사"), 1L),
                new Section(2L, new Station("사"), new Station("라"), 1L)
        );

        final Map<Long, Sections> sectionsByLine = sectionList.stream()
                .collect(Collectors.groupingBy(Section::getLineId, Collectors.collectingAndThen(
                        Collectors.toList(),
                        Sections::new
                )));

        final RouteDto route = shortPathAdapter.findSortPath(new Station("가"), new Station("라"), sectionsByLine);

        assertAll(
                () -> assertThat(route.getLineIds()).hasSize(2),
                () -> assertThat(route.getStations()).usingRecursiveComparison().isEqualTo(List.of(
                        new Station("가"),
                        new Station("나"),
                        new Station("다"),
                        new Station("사"),
                        new Station("라"))
                )
        );
    }

    @Test
    @DisplayName("출발지와 목적지가 등록되어있지 않으면 예외처리")
    void findSortPath_NoSuchStationException() {
        List<Section> sectionList = Arrays.asList(
                new Section(1L, new Station("가"), new Station("나"), 5L),
                new Section(1L, new Station("나"), new Station("다"), 5L),
                new Section(1L, new Station("다"), new Station("라"), 5L),
                new Section(1L, new Station("라"), new Station("마"), 5L),
                new Section(1L, new Station("마"), new Station("바"), 5L),
                new Section(2L, new Station("다"), new Station("사"), 1L),
                new Section(2L, new Station("사"), new Station("라"), 1L)
        );

        final Map<Long, Sections> sectionsByLine = sectionList.stream()
                .collect(Collectors.groupingBy(Section::getLineId, Collectors.collectingAndThen(
                        Collectors.toList(),
                        Sections::new
                )));

        assertThatThrownBy(
                () -> shortPathAdapter.findSortPath(new Station("가"), new Station("비버"), sectionsByLine)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("입력된 역이 없습니다.");
    }
}