package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static subway.domain.fixture.StationFixture.삼성역;
import static subway.domain.fixture.StationFixture.잠실새내역;
import static subway.domain.fixture.StationFixture.잠실역;
import static subway.domain.fixture.StationFixture.종합운동장역;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.fare.FareCalculator;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.dto.request.ShortestPathRequest;
import subway.dto.response.ShortestPathResponse;
import subway.exception.NotFoundStationException;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.StationRepository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("PathService 은(는)")
class PathServiceTest {

    private final StationRepository stationRepository = mock(StationRepository.class);
    private final LineRepository lineRepository = mock(LineRepository.class);
    private final PathFinder pathFinder = mock(PathFinder.class);
    private final FareCalculator fareCalculator = mock(FareCalculator.class);

    private PathService pathService = new PathService(stationRepository, lineRepository, pathFinder, fareCalculator);

    @Test
    void 최단_거리와_금액을_반환한다() {
        // given
        Sections sections = new Sections(
                List.of(
                        new Section(삼성역, 종합운동장역, 2),
                        new Section(종합운동장역, 잠실새내역, 3),
                        new Section(잠실새내역, 잠실역, 4)
                ));
        Lines lines = new Lines(List.of(new Line("2호선", sections)));
        given(stationRepository.findByName("삼성역"))
                .willReturn(Optional.of(삼성역));
        given(stationRepository.findByName("잠실역"))
                .willReturn(Optional.of(잠실역));
        given(pathFinder.findShortestPath(삼성역, 잠실역, lines))
                .willReturn(new Path(sections, 9));
        given(fareCalculator.calculate(any()))
                .willReturn(1250);
        given(lineRepository.findAll())
                .willReturn(lines);

        // when
        ShortestPathResponse response = pathService.findShortestPath(new ShortestPathRequest("삼성역", "잠실역", 30));

        // then
        verify(stationRepository, times(2)).findByName(any());
        verify(pathFinder, times(1)).findShortestPath(any(), any(), any());
        verify(fareCalculator, times(1)).calculate(any());
        verify(lineRepository, times(1)).findAll();
        assertThat(response.getSectionQueryResponses().size()).isEqualTo(3);
        assertThat(response.getTotalDistance()).isEqualTo(9);
        assertThat(response.getFee()).isEqualTo(1250);
    }

    @Test
    void 시작역이_존재하지_않으면_예외() {
        // given
        given(stationRepository.findByName("잠실역"))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(new ShortestPathRequest("잠실역", "사당역", 30)))
                .isInstanceOf(NotFoundStationException.class);
        verify(stationRepository, times(1)).findByName(any());
    }

    @Test
    void 도착역이_존재하지_않으면_예외() {
        // given
        given(stationRepository.findByName("잠실역"))
                .willReturn(Optional.of(잠실역));
        given(stationRepository.findByName("사당역"))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(new ShortestPathRequest("잠실역", "사당역", 30)))
                .isInstanceOf(NotFoundStationException.class);
        verify(stationRepository, times(2)).findByName(any());
    }
}
