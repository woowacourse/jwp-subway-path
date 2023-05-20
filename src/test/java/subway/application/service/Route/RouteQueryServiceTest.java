package subway.application.service.Route;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.adapter.in.web.route.dto.FindShortCutRequest;
import subway.adapter.out.graph.dto.RouteDto;
import subway.application.port.out.graph.ShortPathPort;
import subway.application.port.out.line.LineQueryPort;
import subway.application.port.out.section.SectionQueryPort;
import subway.application.port.out.station.StationQueryPort;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RouteQueryServiceTest {
    @Mock
    private ShortPathPort shortPathPort;
    @Mock
    private SectionQueryPort sectionQueryPort;
    @Mock
    private StationQueryPort stationQueryPort;
    @Mock
    private LineQueryPort lineQueryPort;
    @InjectMocks
    private RouteQueryService routeQueryService;

    @Test
    @DisplayName("노선에 저장된 구간을 입력 시 정상적으로 실행 테스트")
    void findResultShotCut() {
        List<Section> sections = Arrays.asList(
                new Section(1L, new Station("가"), new Station("나"), 5L),
                new Section(1L, new Station("나"), new Station("다"), 5L),
                new Section(1L, new Station("다"), new Station("라"), 5L),
                new Section(1L, new Station("라"), new Station("마"), 5L),
                new Section(1L, new Station("마"), new Station("바"), 5L),
                new Section(2L, new Station("다"), new Station("사"), 1L),
                new Section(2L, new Station("사"), new Station("라"), 1L)
        );

        given(sectionQueryPort.findAll())
                .willReturn(sections);
        given(stationQueryPort.findByName(any()))
                .willReturn(Optional.of(new Station("가")));
        given(stationQueryPort.findByName(any()))
                .willReturn(Optional.of(new Station("라")));
        given(lineQueryPort.findLinesById(List.of(1L, 2L)))
                .willReturn(List.of(new Line(1L, "1호선", 100), new Line(2L, "2호선", 100)));
        given(shortPathPort.findSortPath(any(), any(), anyMap()))
                .willReturn(new RouteDto(
                        List.of(new Station("가"),
                                new Station("나"),
                                new Station("다"),
                                new Station("사"),
                                new Station("라")
                        ),
                        1250, Set.of(1L, 2L))
                );

        assertThatNoException().isThrownBy(
                () -> routeQueryService.findRouteResult(new FindShortCutRequest("가", "라", 25))
        );
    }

    @Test
    @DisplayName("노선에 저장된 역 없을 때 에외처리 테스트")
    void findResultShotCut_NoSuchStation() {
        given(stationQueryPort.findByName(any()))
                .willReturn(Optional.empty());
        given(stationQueryPort.findByName(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(
                () -> routeQueryService.findRouteResult(new FindShortCutRequest("가", "라", 55))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 역입니다.");
    }

    @Test
    @DisplayName("노선이 없을 때 에외처리 테스트")
    void findResultShotCut_NoSuchSection() {
        given(sectionQueryPort.findAll())
                .willReturn(Collections.emptyList());
        given(stationQueryPort.findByName(any()))
                .willReturn(Optional.of(new Station("가")));
        given(stationQueryPort.findByName(any()))
                .willReturn(Optional.of(new Station("라")));

        assertThatThrownBy(
                () -> routeQueryService.findRouteResult(new FindShortCutRequest("가", "라", 99))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("노선에 구간을 추가해주세요");
    }
}