package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static subway.repository.DomainFixtures.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.fare.FarePolicy;
import subway.domain.line.Line;
import subway.domain.line.Station;
import subway.dto.path.ShortestPathSelectResponse;
import subway.dto.station.StationSelectResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PathServiceTest {

    LineRepository lineRepository = mock(LineRepository.class);
    StationRepository stationRepository = mock(StationRepository.class);
    FarePolicy farePolicy = new FarePolicy();
    PathService pathService = new PathService(lineRepository, stationRepository, farePolicy);

    private final List<Line> lines = List.of(
            new Line("2호선",
                    List.of(
                            서초_교대_거리7_구간,
                            교대_강남_거리12_구간,
                            강남_역삼_거리8_구간,
                            역삼_선릉_거리12_구간
                    )),
            new Line("3호선",
                    List.of(
                            신사_잠원_거리15_구간,
                            잠원_고속터미널_거리9_구간,
                            고속터미널_교대_거리12_구간
                    )),
            new Line("9호선",
                    List.of(
                            고속터미널_사평_거리8_구간,
                            사평_신논현_거리11_구간
                    )),
            new Line("신분당선",
                    List.of(
                            신사_논현_거리7_구간,
                            논현_신논현_거리8_구간,
                            신논현_강남_거리9_구간
                    ))
    );

    @BeforeEach
    void setUp() {
        given(lineRepository.findAll()).willReturn(lines);
    }

    @Test
    void 역과_역_사이의_최단거리를_조회한다() {
        // given
        Long 역삼역_id = 역삼역.getId();
        Long 신논현역_id = 신논현역.getId();
        given(stationRepository.findById(역삼역_id)).willReturn(Optional.of(new Station(역삼역_id, "역삼역")));
        given(stationRepository.findById(신논현역_id)).willReturn(Optional.of(new Station(신논현역_id, "신논현역")));

        // when
        final ShortestPathSelectResponse shortestPath = pathService.findShortestPath(역삼역_id, 신논현역_id);

        // then
        System.out.println("shortestPath = " + shortestPath);
        assertThat(shortestPath.getPath()).map(StationSelectResponse::getName)
                .containsExactly("역삼역", "강남역", "신논현역");
        assertThat(shortestPath.getDistance()).isEqualTo(17);
        assertThat(shortestPath.getFare()).isEqualTo(1_450);

    }
}
