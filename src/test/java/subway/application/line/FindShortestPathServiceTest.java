package subway.application.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.path.service.FindShortestPathService;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;
import subway.domain.station.StationRepository;
import subway.ui.dto.response.PathResponse;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class FindShortestPathServiceTest {


    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private FindShortestPathService findShortestPathService;

    @BeforeEach
    void setUp() {
        findShortestPathService = new FindShortestPathService(
                lineRepository,
                stationRepository
        );
    }

    @Test
    void 최단_경로_조회_테스트() {
        //given
        final Line firstLine = firstLineFixture();   // 강남 --3-- 역삼 --2-- 선릉
        final Line secondLine = secondLineFixture(); // 강남구청 --5-- 선정릉 --8-- 선릉
        given(lineRepository.findAll()).willReturn(List.of(firstLine, secondLine));
        given(stationRepository.findById(1L)).willReturn(Optional.of(new Station("역삼")));
        given(stationRepository.findById(2L)).willReturn(Optional.of(new Station("선정릉")));

        //when
        final PathResponse shortestPath = findShortestPathService.findShortestPath(1L, 2L);

        //then
        assertThat(shortestPath.getStations()).extracting("stationName")
                .containsExactly("역삼", "선릉", "선정릉");
        assertThat(shortestPath.getDistance()).isEqualTo(2 + 8);

    }

    private Line firstLineFixture() {
        final Sections sections = new Sections(List.of(
                new Section(new Station("강남"), new Station("역삼"), new StationDistance(3)),
                new Section(new Station("역삼"), new Station("선릉"), new StationDistance(2))
        ));
        return new Line(sections, new LineName("1호선"), new LineColor("파랑색"));
    }

    private Line secondLineFixture() {
        final Sections sections = new Sections(List.of(
                new Section(new Station("강남구청"), new Station("선정릉"), new StationDistance(5)),
                new Section(new Station("선정릉"), new Station("선릉"), new StationDistance(8))
        ));

        return new Line(sections, new LineName("2호선"), new LineColor("청록색"));
    }
}
