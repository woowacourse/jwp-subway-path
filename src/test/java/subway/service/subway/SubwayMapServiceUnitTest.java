package subway.service.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.dto.route.ShortestPathRequest;
import subway.dto.route.ShortestPathResponse;
import subway.entity.LineEntity;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.service.SubwayMapService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static subway.fixture.LineFixture.createLine;
import static subway.fixture.SectionsFixture.createSections;

@ExtendWith(MockitoExtension.class)
class SubwayMapServiceUnitTest {

    @InjectMocks
    private SubwayMapService subwayMapService;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private LineRepository lineRepository;

    @Test
    @DisplayName("지하철역의 역 정보를 순서대로 보여준다.")
    void show_ordered_station_map_success() {
        // given
        long lineNumber = 2;
        Sections sections = new Sections(
                List.of(
                        new Section(
                                new Station("잠실역"),
                                new Station("잠실새내역"),
                                3)
                )
        );
        given(sectionRepository.findSectionsByLineNumber(lineNumber)).willReturn(sections);

        // when
        subwayMapService.showLineMapByLineNumber(2L);

        // then
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("최단 경로를 반환한다.")
    void returns_shortest_path() {
        // given
        ShortestPathRequest req = new ShortestPathRequest("잠실역", "종합운동장역");
        List<LineEntity> lineEntities = List.of(new LineEntity(1L, 2, "2호선", "green"));
        Sections sections = createSections();

        given(lineRepository.findAll()).willReturn(lineEntities);
        given(sectionRepository.findSectionsByLineNumber(2L)).willReturn(sections);
        given(lineRepository.findByLineNameAndSections("2호선", sections)).willReturn(createLine());

        // when
        ShortestPathResponse shortestPath = subwayMapService.findShortestPath(req);

        // then
        assertAll(
                () -> assertThat(shortestPath.getStations().size()).isEqualTo(3),
                () -> assertThat(shortestPath.getStations().get(0).getStation().getName()).isEqualTo("잠실역"),
                () -> assertThat(shortestPath.getStations().get(2).getStation().getName()).isEqualTo("종합운동장역")
        );
    }
}
