package subway.application;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.fare.FarePolicy;
import subway.dto.LineAndSectionsResponse;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static subway.common.fixture.DomainFixture.이호선_초록색_침착맨_디노_로운;
import static subway.common.fixture.DomainFixture.일호선_남색_후추_디노_조앤;
import static subway.common.fixture.DomainFixture.침착맨;
import static subway.common.fixture.DomainFixture.침착맨_디노;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.DomainFixture.후추_디노;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private FarePolicy farePolicy;

    @InjectMocks
    private PathService pathService;

    @Test
    void 최단_경로를_찾는다() {
        //given
        final PathRequest pathRequest = new PathRequest(후추.getId(), 침착맨.getId());
        final int fare = 1350;

        when(lineRepository.findLines())
                .thenReturn(List.of(일호선_남색_후추_디노_조앤, 이호선_초록색_침착맨_디노_로운));

        when(lineRepository.findLineById(일호선_남색_후추_디노_조앤.getId()))
                .thenReturn(일호선_남색_후추_디노_조앤);

        when(lineRepository.findLineById(이호선_초록색_침착맨_디노_로운.getId()))
                .thenReturn(이호선_초록색_침착맨_디노_로운);

        when(stationRepository.findStationById(후추.getId()))
                .thenReturn(후추);

        when(stationRepository.findStationById(침착맨.getId()))
                .thenReturn(침착맨);

        when(farePolicy.calculate(any()))
                .thenReturn(fare);

        //when
        final PathResponse pathResponse = pathService.findShortestPath(pathRequest);

        //then
        assertThat(pathResponse).usingRecursiveComparison()
                .isEqualTo(new PathResponse(
                        List.of(
                                LineAndSectionsResponse.of(일호선_남색_후추_디노_조앤, List.of(후추_디노)),
                                LineAndSectionsResponse.of(이호선_초록색_침착맨_디노_로운, List.of(침착맨_디노))
                        ),
                        12,
                        1350
                ));
    }
}
